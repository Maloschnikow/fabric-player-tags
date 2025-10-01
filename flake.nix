{
  description = "JDK 21";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-24.11";
  };

  outputs = { self, nixpkgs }:

    let 
      system = "x86_64-linux";
      pkgs = nixpkgs.legacyPackages.${system};

      # https://gist.github.com/Lgmrszd/98fb7054e63a7199f9510ba20a39bc67
      libs = with pkgs; [
        libpulseaudio
        libGL
        glfw
        openal
        stdenv.cc.cc.lib
      ];

    in
    {
      devShells.${system}.default =
        pkgs.mkShellNoCC {

          packages = with pkgs; [
            jdk21
          ];

          buildInputs = libs;

          LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath libs;

      };
    };
}
