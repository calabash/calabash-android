package sh.calaba.instrumentationbackend.actions.ipc;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by john7doe on 22/01/15.
 */

public class SendTcp implements Action {
    private static Map<Integer, Socket> connections = new HashMap<Integer, Socket>();

    @java.lang.Override
    public Result execute(String... args) {
        if(args.length < 2 || args.length > 3) {
            return Result.failedResult("Wrong number of arguments. Expected: port, command, [keepConnectionAlive]");
        }

        int port = Integer.parseInt(args[0]);
        String command = args[1];
        boolean keepConnectionAlive = args.length == 3 && Boolean.parseBoolean(args[2]);

        PrintWriter out = null;
        BufferedReader in = null;

        try {
            boolean hasExistingConnection = connections.containsKey(port);

            Socket socket;
            if (!hasExistingConnection) {
                socket = new Socket(InetAddress.getLocalHost(), port);
                socket.setKeepAlive(keepConnectionAlive);
                if(keepConnectionAlive) {
                    connections.put(port, socket);
                }
            } else {
                socket = connections.get(port);
            }

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(command);
            return Result.successResult(in.readLine());
        } catch (IOException e) {
            return Result.fromThrowable(e);
        } finally {
            if(!keepConnectionAlive) {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    // Ignore
                } finally {
                    if (out != null) {
                        out.close();
                    }
                    connections.remove(port);
                }
            }
        }
    }

    @java.lang.Override
    public String key() {
        return "send_tcp";
    }
}
