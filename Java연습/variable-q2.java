public class Main {
    public static void main(String[] args) {
        String name = "green";
        int age = 22;
        double height = 161.154321;
        String heightS = String.format("%.1f", height); //소수점 둘째자리에서 반올림

        System.out.println(name+", "+age+", "+heightS);
    }
}
