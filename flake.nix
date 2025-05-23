{
  description = "JDK 21";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-24.11";
  };

  outputs = { self, nixpkgs }:

    let 
      system = "x86_64-linux";
      pkgs = nixpkgs.legacyPackages.${system};

    in
    {
      devShells.${system}.default =
        pkgs.mkShellNoCC {

          packages = with pkgs; [
            jdk21
          ];

      };
    };
}
