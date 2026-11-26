
package cl.miespacio;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiEspacioApplication {

    public static void main(String[] args) {

        configurarWallet();

        SpringApplication.run(MiEspacioApplication.class, args);
    }

    private static void configurarWallet() {

        try {
            Path ruta = Paths.get(
                MiEspacioApplication.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
            );

            if (!Files.isDirectory(ruta)) {
                ruta = ruta.getParent();
            }

            while (ruta != null) {

                Path wallet = ruta.resolve(
                    "Wallet_A6ETG25WWEMT8HFB"
                );

                if (Files.isDirectory(wallet)) {

                    System.setProperty(
                        "oracle.net.tns_admin",
                        wallet.toAbsolutePath().toString()
                    );

                    System.out.println(
                        "Wallet Oracle encontrada: " + wallet
                    );

                    return;
                }

                ruta = ruta.getParent();
            }

            throw new IllegalStateException(
                "No se encontró la Wallet de Oracle."
            );

        } catch (Exception e) {
            throw new IllegalStateException(
                "Error al configurar la Wallet de Oracle.", e
            );
        }
    }
}
