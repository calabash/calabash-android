module Calabash
  module Android
    # A module for managing the ~/.test-cloud-dev directory.
    module DotDir
      def self.directory
        home = Calabash::Android::Environment.user_home_directory
        dir = File.join(home, ".test-cloud-dev")
        if !File.exist?(dir)
          FileUtils.mkdir_p(dir)
        end
        dir
      end
    end
  end
end


