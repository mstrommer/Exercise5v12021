package at.ac.fhcampuswien.tictactoe;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Timeout(2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TicTacToeTest {

    private PrintStream originalOut;
    private InputStream originalIn;
    private ByteArrayOutputStream bos;
    private PrintStream ps;

    @BeforeAll
    public static void init() {
        System.out.println("Testing Exercise 5");
    }

    @AfterAll
    public static void finish() {
        System.out.println("Finished Testing Exercise 5");
    }

    @BeforeEach
    public void setupStreams() throws IOException {
        originalOut = System.out;
        originalIn = System.in;

        bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);
        System.setIn(pis);
        ps = new PrintStream(pos, true);
    }

    @AfterEach
    public void tearDownStreams() {
        // undo the binding in System
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    @Order(1)
    public void checkPlayerClass1() {
        try {
            // check if there are already fields declared
            assertTrue(Player.class.getDeclaredFields().length != 0,"Class Person hasn't declared any members yet.");
            // check if all fields are named correctly, private,...
            assertTrue(Arrays.stream(Player.class.getDeclaredFields()).allMatch(
                    field -> Modifier.toString(field.getModifiers()).equals("private") && (
                            field.getName().equals("symbol") || field.getName().equals("name"))
            ), "Please check your field names and modifiers!");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Order(2)
    public void checkPlayerClass2() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.tictactoe.Player");
            assertEquals(1,
                    Arrays.stream(c.getConstructors()).filter(constructor
                            -> constructor.toString().equals("public at.ac.fhcampuswien.tictactoe.Player(char,java.lang.String)")).count(),
                    "Constructor (char,String) missing");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Order(3)
    public void getName() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.tictactoe.Player");
            Method m = c.getMethod("getName");
            Constructor<?> co = c.getConstructor(char.class,String.class);
            Player p = (Player) co.newInstance('O',"Yoda");
            assertEquals("Yoda", (String) m.invoke(p), "Getter for name not working correctly.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Player.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method called getName().");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    @Order(4)
    public void getSymbol() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.tictactoe.Player");
            Method m = c.getMethod("getSymbol");
            Constructor<?> co = c.getConstructor(char.class,String.class);
            Player p = (Player) co.newInstance('O',"Yoda");
            assertEquals('O', (char) m.invoke(p), "Getter for symbol not working correctly.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Player.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method called getSymbol().");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    @Order(5)
    public void makeMove() {
        try {
            ps.println(19);
            ps.println(-1);
            ps.println(0);
            ps.println(1);

            // Expected
            String output = "Yoda make your move (choose a number): Yoda make your move (choose a number): Yoda make your move (choose a number): Yoda make your move (choose a number): ";

            Class<?> c = Class.forName("at.ac.fhcampuswien.tictactoe.Player");
            Method m = c.getMethod("makeMove", TicTacToe.class);
            Constructor<?> co = c.getConstructor(char.class,String.class);
            Player p1 = (Player) co.newInstance('O',"Yoda");
            Player p2 = (Player) co.newInstance('X',"Boba");
            Class<?> c2 = Class.forName("at.ac.fhcampuswien.tictactoe.TicTacToe");
            Constructor<?> co2 = c2.getConstructor(Player.class,Player.class);
            TicTacToe ttt = (TicTacToe) co2.newInstance(p1,p2);

            // Action
            m.invoke(p1,ttt);

            // Assertion
            assertEquals(output, bos.toString());
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Player and TicTacToe with corresponding constructors.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method called makeMove(TicTacToe game).");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    @Order(6)
    public void checkTicTacToeClass1() {
        try {
            // check if there are already fields declared
            assertTrue(TicTacToe.class.getDeclaredFields().length != 0,"Class Person hasn't declared any members yet.");
            // check if all fields are named correctly, private,...
            assertTrue(Arrays.stream(TicTacToe.class.getDeclaredFields()).allMatch(
                    field -> field.getName().equals("ROWS") || field.getName().equals("COLS") || field.getName().equals("gameBoard")
                            || field.getName().equals("p1") || field.getName().equals("p2")
            ), "Please check your field names (ROWS, COLS, gameBoard,...) in Class TicTacToe!");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Order(7)
    public void checkTicTacToeClass2() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.tictactoe.TicTacToe");
            assertEquals(1,
                    Arrays.stream(c.getConstructors()).filter(constructor
                            -> constructor.toString().equals("public at.ac.fhcampuswien.tictactoe.TicTacToe(at.ac.fhcampuswien.tictactoe.Player,at.ac.fhcampuswien.tictactoe.Player)")).count(),
                    "Constructor (Player,Player) missing");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Order(8)
    public void placeTic() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.tictactoe.Player");
            Constructor<?> co = c.getConstructor(char.class,String.class);
            Player p1 = (Player) co.newInstance('O',"Yoda");
            Player p2 = (Player) co.newInstance('X',"Boba");
            Class<?> c2 = Class.forName("at.ac.fhcampuswien.tictactoe.TicTacToe");
            Constructor<?> co2 = c2.getConstructor(Player.class,Player.class);
            TicTacToe ttt = (TicTacToe) co2.newInstance(p1,p2);
            Method m = c2.getMethod("placeTic", at.ac.fhcampuswien.tictactoe.Player.class, int.class, int.class);
            assertTrue((boolean) m.invoke(ttt, p1, 0, 0), "PlaceTic should work placing a tic at position (0,0) the first time.");
            assertFalse((boolean) m.invoke(ttt, p1, 0, 0), "PlaceTic should return false trying to place a tic on an existing tic.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Player and TicTacToe with corresponding constructors.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method called placeTic().");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    @Order(9)
    public void prettyPrintGameBoard() {
        try {
            // Expected
            String output = "1 | 2 | 3 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "4 | 5 | 6 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator();


            Class<?> c = Class.forName("at.ac.fhcampuswien.tictactoe.Player");
            Constructor<?> co = c.getConstructor(char.class,String.class);
            Player p1 = (Player) co.newInstance('O',"Yoda");
            Player p2 = (Player) co.newInstance('X',"Boba");
            Class<?> c2 = Class.forName("at.ac.fhcampuswien.tictactoe.TicTacToe");
            Constructor<?> co2 = c2.getConstructor(Player.class,Player.class);
            TicTacToe ttt = (TicTacToe) co2.newInstance(p1,p2);
            Method m = c2.getDeclaredMethod("prettyPrintGameBoard");
            m.setAccessible(true);

            // Action
            m.invoke(ttt);

            // Assertion
            assertEquals(output, bos.toString());
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Player and TicTacToe with corresponding constructors.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method (private, void) called prettyPrintGameBoard().");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    @Order(10)
    public void checkIfWon() {
        try {
            Class<?> c = Class.forName("at.ac.fhcampuswien.tictactoe.Player");
            Constructor<?> co = c.getConstructor(char.class,String.class);
            Player p1 = (Player) co.newInstance('O',"Yoda");
            Player p2 = (Player) co.newInstance('X',"Boba");
            Class<?> c2 = Class.forName("at.ac.fhcampuswien.tictactoe.TicTacToe");
            Constructor<?> co2 = c2.getConstructor(Player.class,Player.class);
            TicTacToe ttt = (TicTacToe) co2.newInstance(p1,p2);
            Method m = c2.getMethod("checkIfWon", Player.class);
            Field privateField = TicTacToe.class.getDeclaredField("gameBoard");
            privateField.setAccessible(true);
            char [][] gameBoard = (char[][]) privateField.get(ttt);;
            gameBoard[0][0] = 'X';
            gameBoard[0][1] = 'X';
            gameBoard[0][2] = 'X';

            // Assertion
            assertTrue((boolean)m.invoke(ttt,p2), "checkIfWon seems not to work correctly. (First row with same symbols!)");
            assertFalse((boolean)m.invoke(ttt,p1), "checkIfWon seems not to work correctly. (Player did not win!)");

            gameBoard[0][0] = 'O';
            gameBoard[1][1] = 'O';
            gameBoard[2][2] = 'O';

            assertTrue((boolean)m.invoke(ttt,p1), "checkIfWon seems not to work correctly. (Diagonal with same symbols!)");

        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Player and TicTacToe with corresponding constructors.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method (public boolean) called checkIfWon().");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    @Order(11)
    public void playGame1() {
        try {
            ps.println(1);
            ps.println(2);
            ps.println(3);
            ps.println(4);
            ps.println(5);
            ps.println(6);
            ps.println(7);

            // Expected
            String output = "1 | 2 | 3 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "4 | 5 | 6 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Yoda make your move (choose a number): O | 2 | 3 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "4 | 5 | 6 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Boba make your move (choose a number): O | X | 3 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "4 | 5 | 6 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Yoda make your move (choose a number): O | X | O " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "4 | 5 | 6 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Boba make your move (choose a number): O | X | O " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "X | 5 | 6 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Yoda make your move (choose a number): O | X | O " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "X | O | 6 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Boba make your move (choose a number): O | X | O " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "X | O | X " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Yoda make your move (choose a number): Yoda has won the game!" + System.lineSeparator() +
                    "O | X | O " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "X | O | X " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "O | 8 | 9 " + System.lineSeparator();

            Class<?> c = Class.forName("at.ac.fhcampuswien.tictactoe.Player");
            Constructor<?> co = c.getConstructor(char.class,String.class);
            Player p1 = (Player) co.newInstance('O',"Yoda");
            Player p2 = (Player) co.newInstance('X',"Boba");
            Class<?> c2 = Class.forName("at.ac.fhcampuswien.tictactoe.TicTacToe");
            Constructor<?> co2 = c2.getConstructor(Player.class,Player.class);
            TicTacToe ttt = (TicTacToe) co2.newInstance(p1,p2);
            Method m = c2.getMethod("playGame");

            // Action
            m.invoke(ttt);

            // Assertion
            assertEquals(output, bos.toString());
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Player and TicTacToe with corresponding constructors.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method (public boolean) called checkIfWon().");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }

    @Test
    @Order(12)
    public void playGame2() {
        try {
            ps.println(1);
            ps.println(2);
            ps.println(3);
            ps.println(5);
            ps.println(4);
            ps.println(6);
            ps.println(9);
            ps.println(7);
            ps.println(8);

            // Expected
            String output = "1 | 2 | 3 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "4 | 5 | 6 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Yoda make your move (choose a number): O | 2 | 3 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "4 | 5 | 6 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Boba make your move (choose a number): O | X | 3 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "4 | 5 | 6 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Yoda make your move (choose a number): O | X | O " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "4 | 5 | 6 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Boba make your move (choose a number): O | X | O " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "4 | X | 6 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Yoda make your move (choose a number): O | X | O " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "O | X | 6 " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Boba make your move (choose a number): O | X | O " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "O | X | X " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | 9 " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Yoda make your move (choose a number): O | X | O " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "O | X | X " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "7 | 8 | O " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Boba make your move (choose a number): O | X | O " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "O | X | X " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "X | 8 | O " + System.lineSeparator() +
                    "" + System.lineSeparator() +
                    "Yoda make your move (choose a number): Nobody wins!" + System.lineSeparator() +
                    "O | X | O " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "O | X | X " + System.lineSeparator() +
                    "--+---+--" + System.lineSeparator() +
                    "X | O | O " + System.lineSeparator();

            Class<?> c = Class.forName("at.ac.fhcampuswien.tictactoe.Player");
            Constructor<?> co = c.getConstructor(char.class,String.class);
            Player p1 = (Player) co.newInstance('O',"Yoda");
            Player p2 = (Player) co.newInstance('X',"Boba");
            Class<?> c2 = Class.forName("at.ac.fhcampuswien.tictactoe.TicTacToe");
            Constructor<?> co2 = c2.getConstructor(Player.class,Player.class);
            TicTacToe ttt = (TicTacToe) co2.newInstance(p1,p2);
            Method m = c2.getMethod("playGame");

            // Action
            m.invoke(ttt);

            // Assertion
            assertEquals(output, bos.toString());
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            fail("There should be a class called Player and TicTacToe with corresponding constructors.");
        } catch (NoSuchMethodException nsme){
            nsme.printStackTrace();
            fail("There should be a method (public boolean) called checkIfWon().");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problems might have occurred creating the Object. Also check return types.");
        }
    }
}