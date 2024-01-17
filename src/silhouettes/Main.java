package silhouettes;

import java.awt.image.BufferedImage;

public class Main{
    public static void main(String[] args) {
        // Створення екземпляра ImageProcessor для завантаження зображення
        ImageProcessor1 imageProcessor = new ImageProcessor1();

        // Завантажте зображення з указаного шляху до файлу або використовуйте шлях за замовчуванням,
        // якщо шлях не вказано
        BufferedImage image = imageProcessor.loadImage(args);

        // Перевірте, чи успішно завантажено зображення
        if (image != null) {
            // Створення екземпляра SilhouetteDetector для пошуку силуетів на зображенні
            OurGoodVariant silhouetteDetector = new OurGoodVariant();

            // Знайти кількість силуетів на зображенні
            int number = silhouetteDetector.findSilhouettes(image);

            System.out.println("Number of Silhouettes: " + number);
        }
    }
}
