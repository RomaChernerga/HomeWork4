package HomeWork4;

import java.util.Random;
import java.util.Scanner;

public class HomeWork_4 {
    private static final char DOT_HUMAN = 'X';// ввод константы, отображения занятой юзером ячейки...
    private static final char DOT_AI = '0'; // ввод константы, отображения занятой компьютером ячейки...
    private static final char DOT_EMPTY = '-'; // ввод константы, пустое значение в поле...
    private static final Scanner SCANNER = new Scanner(System.in); //ввод сканера в код(где будут считываться ходы игроков)
    private static final Random RANDOM = new Random(); // чтобы делать ход компьютером;
    private static char[][] field;  // сообщаем, что в коде будет двумерный массив(в кач-ве поля);

    private static int fieldSizeX; //размер поля по горизонтали
    private static int fieldSizeY; //размер поля по вертикали
    private static int scoreHuman;  //ввод переменной для подсчета очков юзера
    private static int scoreAI;  //ввод переменной для подсчета очков компьютера
    private static int roundCounter = 1;
    private static int winLenghth = 5;

    public static void main(String[] args) {

        playRound();  // объединение в метод
        play();
    }

    private static void play(){
        while (true){
            playRound();
            System.out.printf("SCORE: HUMAN      AI\n" +
                    "             %d        %d\n", scoreHuman, scoreAI);
            System.out.print ("Wanna play again ? Y or N >>> ");
            if(!SCANNER.next().toLowerCase().equals("y")){
                System.out.println("Good bye !");
                break;
            }
        }
    }

    private static void playRound() {
        System.out.printf("ROUND %d\n", roundCounter++);
        initField(9, 9);   // ввод параметров размера поля
        printField();   // команда для печати поля!

        while (true) {     // заключаем в блок и зацикливаем
            humanTurn();   // вводим данные от юзера в поле.
            printField();  // команда для печати поля!
            if (checkGame(DOT_HUMAN)) break;
                AITurn();   //ход компьютера
                printField();  //обновление поля

            if (checkGame(DOT_AI)) break;;


            if (checkDraw()) break;
        }
    }

    private static boolean checkGame(char dot) {
        if (checkDraw()) return true;
        if (checkWin(dot, winLenghth)) {
            if (dot == DOT_HUMAN) {
                System.out.println("HUMAN WINS !!!");
                scoreHuman++;
            } else {
                System.out.println("AI WINS !!!");
                scoreAI++;
            }
            return true;
        }
        return false;
    }

    private static void humanTurn() {    // запрос у юзера на первый и последующие ходы
        int x, y;   // запрос координат хода

        do {   // требование  просить юзера, вводить координаты, пока.....
            System.out.print("Please enter coordinates of your turn x & y split by whitespace >>>>");
            x = SCANNER.nextInt() - 1;  // координаты вводятся с -1, т.к. отсчет с 0 происходит
            y = SCANNER.nextInt() - 1;  // координаты вводятся с -1, т.к. отсчет с 0 происходит
        } while (!isCellEValid(y, x) || !isCellEmpty(y, x)); // ...пока ячейка НЕсвободная или НЕпустая
        field[y][x] = DOT_HUMAN; // ставим в координаты юзера его фишку/обозначение
    }

    private static void AITurn(){
        for (int i = 0; winLenghth - i > 2; i++){
            if(scanField(DOT_AI, winLenghth - i)) return;  // проверка выигрыша компьютера
            if(scanField(DOT_HUMAN, winLenghth - i)) return;  // проверка выигрыша игрока
        }
        AITurnEasy(); // в противном случае компбютер ставит фишку на случ ячейке
    }

    private static void AITurnEasy() {   // запрос у компьютера на первый и последующие ходы
        int x, y;   // запрос координат хода
        do {                // требование  просить компьютер, вводить координаты, пока.....
            x = RANDOM.nextInt(fieldSizeX);   // рандомные координаты от компьютера
            y = RANDOM.nextInt(fieldSizeY);   // рандомные координаты от компьютера
        } while (!isCellEmpty(y, x));    // пока координаты YX НЕпустые !!!!
        field[y][x] = DOT_AI;             // ставим в координаты компьютера его фишку/обозначение
    }

    private static boolean checkDraw() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (!isCellEmpty(y, x)) return false;
            }
        }
        System.out.println("DRAW!!!");
        return true;
    }

    private static boolean scanField(char dot, int length){ // сканируем поле на фишки
        for(int y = 0; y < fieldSizeY; y++){    // проходимся по Y
            for(int x = 0; x < fieldSizeX; x++) {  // проходимся по X
                if(isCellEmpty(y, x)) {  // если ячейка пустая есть, то
                    field[y][x] = dot;   // ставим фишку на пуст ячейку
                    if(checkWin(dot, length)) { // если метод может дать нам победу, то
                        if (dot == DOT_AI) return true; // если комп выигрывает, поставив там фишку - ставим ее туда
                        if(dot == DOT_HUMAN){  // если игрок выигрывает на этом месте
                            field[y][x] = DOT_AI; // ставим туда фишку
                            return true;
                        }
                    }
                    field[y][x] = DOT_EMPTY; // если на этой клетке комп или игрок не выиграют на проверяемой клетке, идем дальше.
                }
            }
        }
        return false;
    }

    private static boolean checkWin(char dot, int length) {   // Метод для проверки побед или проигрыша
      for(int y = 0; y < fieldSizeY; y++){   // проверка по y
          for(int x = 0; x < fieldSizeX; x++){  // проверка по x
              if(checkLine(x, y, 1, 0, length, dot)) return true; // проверка линии по +X
              if(checkLine(x, y, 1, 1, length, dot)) return true; // проверка по диагонали +x+y
              if(checkLine(x, y, 0, 1, length, dot)) return true; // проверка линии по +y
              if(checkLine(x, y, 1, -1, length, dot)) return true; // проверка линии по +X-Y

          }
      }
      return false;
    }

    private static boolean checkLine (int x, int y, int transformX, int transformY, int len, char dot){
        int endXLine = x + (len -1) * transformX; // переменная, конец линии по X, чтобы не выйти за пределы массива
        int endYLine = y + (len -1) * transformY; // переменная, конец линии по Y, чтобы не выйти за пределы массива
        if (!isCellEValid(endYLine, endXLine)) return false; //выход линии за пределы поля
        for (int i = 0; i < len; i++){          //проверка по линии циклом
            if(field[y + i * transformY][x + i * transformX] != dot) return false; // проверка в каждой итерации, что в каждой ячейке фишка.
        }
        return true; // если дошли до конца - возвращаем true
    }

    private static boolean isCellEValid(int y, int x) {   // метод для проверки координат юзера!
        return x >= 0 && y >= 0 && x < fieldSizeX && y < fieldSizeY; // проверка что координаты юзера не меньше 0 и не больше размера поля!!!
    }

    private static boolean isCellEmpty(int y, int x) {  // метод для проверки вакантности поля, принимает на вход YX
        return field[y][x] == DOT_EMPTY;  // проверка поля, что оно по координатам YX - свободно(заполненно соответствующим значением)
    }

    private static void initField(int sizeX, int sizeY) {  // построение игрового поля
        fieldSizeY = sizeY;                                 // присуждение переменных по Y
        fieldSizeX = sizeX;                                 // присуждение переменных по X
        field = new char[sizeY][sizeX];                   // поле - символьный массив оп размеру YX
        for (int y = 0; y < fieldSizeY; y++) {       // чтобы создать поле, сначала пробегаемся по массиву
            for (int x = 0; x < fieldSizeX; x++) {       // слева направо и сверху вниз
                field[y][x] = DOT_EMPTY;            // и заполняем точками, присваивая их каждой координате
            }
        }
    }

    static void printField() {                           // метод  пропечатывания поля
        System.out.print("+");
        for (int i = 0; i < fieldSizeX * 2 + 1; i++) {
            System.out.print(i % 2 == 0 ? "-" : i / 2 + 1); //печатаем черточку при четной итерации, а если нечетная печатаем координату
        }
        System.out.println();                             // Перенос строки

        for (int i = 0; i < fieldSizeY; i++) {                  //идем по вертикали
            System.out.print(i + 1 + "|");                // печатаем разделитель вертикальный
            for (int j = 0; j < fieldSizeX; j++) {         //идем по горизонтали, печатаем строку
                System.out.print(field[i][j] + "|");      // печатаем разделитель вертикальный что находится в координате.
            }
            System.out.println();                           // Перенос строки
        }
        for (int i = 0; i <= fieldSizeX * 2 + 1; i++) {      // код для отчерчивания  результатов таблицы, после каждого обновления
            System.out.print("_");                          // печатаем в коде отчерчивание после обновалений таблицы...
        }
        System.out.println();                                // Перенос строки
    }
}

