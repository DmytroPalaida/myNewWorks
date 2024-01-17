package calculator;

/**
 * Interface contain method calculate, and gives possibility to add to list all classes which implements it
 * Інтерфейс містить метод Calculation і дає можливість додати до списку всі класи, які його реалізують
 */
public interface IAction{
    double calculate(double number);
}

/**
 * Calculates sin function
 */
class Sin implements IAction{
    @Override
    public double calculate(double number) {
        return Math.sin(number);

    }
}

/**
 * Calculates cos function
 */
class Cos implements IAction{
    @Override
    public double calculate(double number) {
        return Math.cos(number);
    }
}

/**
 * Calculates tan function
 */
class Tan implements IAction{
    @Override
    public double calculate(double number) {
        return Math.tan(number);
    }
}

/**
 * Calculates atan function
 */
class Atan implements IAction{
    @Override
    public double calculate(double number) {
        return Math.atan(number);
    }
}

/**
 * Calculates log10 function
 */
class Log10 implements IAction{
    @Override
    public double calculate(double number) {
        return Math.log10(number);
    }
}

/**
 * Calculates log2 function
 */
class Log2 implements IAction{
    @Override
    public double calculate(double number) {
        return Math.log(number) / Math.log(2);
    }
}

/**
 * Calculates sqrt function
 */
class Sqrt implements IAction{
    @Override
    public double calculate(double number) {
        return Math.sqrt(number);
    }
}