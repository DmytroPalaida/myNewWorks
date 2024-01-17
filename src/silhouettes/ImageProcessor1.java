package silhouettes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor1{
    /**
     * Шлях до файлу за замовчуванням, який використовується під час завантаження зображення, якщо шлях не вказано.
     */
    private static final String DEFAULT_PATH = "";

    /**
     * Завантажує зображення з указаного шляху до файлу або використовує шлях за замовчуванням, якщо шлях не вказано.
     *
     * @param args Аргументи командного рядка. Перший аргумент, якщо він присутній, використовується як шлях до файлу.
     * @return Завантажене BufferedImage або null, якщо сталася помилка.
     */
    public BufferedImage loadImage(String[] args) {
        // Використовуйте вказаний шлях до файлу або шлях за замовчуванням, якщо його не вказано
        String filePath = (args.length > 0) ? args[0] : DEFAULT_PATH;

        try {
            // Спроба прочитати та повернути BufferedImage із зазначеного файлу
            return ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.err.println("File not found!");
            return null;
        }
    }
}
