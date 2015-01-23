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
            return Result.failedResult("Wrong number of arguments");
        }
        int port = Integer.parseInt(args[0]);
        String command = args[1];

        boolean keepConnectionAlive = false;
        if(args.length == 3) {
            keepConnectionAlive = Boolean.parseBoolean(args[2]);
        }

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

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(command);

            if(!keepConnectionAlive) {
                in.close();
                out.close();
                socket.close();
                connections.remove(port);
            }

            return Result.successResult(in.readLine());
        } catch (IOException e) {
            return Result.fromThrowable(e);
        }
    }

    @java.lang.Override
    public String key() {
        return "send_tcp";
    }
}
