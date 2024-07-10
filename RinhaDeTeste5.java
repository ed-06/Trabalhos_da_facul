package Trabalhos_da_facul;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class RinhaDeTeste5 {
    static char[][] tabuleiro = new char[3][3];
    static boolean vezMaiuscula = false; // Começa com minúscula
    static String usadasMinusculas = "";
    static String usadasMaiusculas = "";
    static Set<Character> letrasMaiusculasUsadas = new HashSet<>();
    static Set<Character> letrasMinusculasUsadas = new HashSet<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarTabuleiro();
        while (true) {
            escreverTabuleiro();
            char jogadorDaVez = vezMaiuscula ? 'M' : 'm';
            System.out.println("Jogador " + jogadorDaVez + ", digite a posição da linha:");
            int linha = scanner.nextInt();
            System.out.println("Jogador " + jogadorDaVez + ", digite a posição da coluna:");
            int coluna = scanner.nextInt();
            System.out.println("Qual a letra vai ser usada:");
            char letra = scanner.next().charAt(0);

            // Verificar se a posição é válida e a letra pode ser colocada
            if (linha >= 0 && linha < 3 && coluna >= 0 && coluna < 3 &&
                    verificarLetras(linha, coluna, letra)) {

                // Verificar se houve vitória
                boolean vitoria = verificarVitoria();

                if (vitoria) {
                    escreverTabuleiro();
                    System.out.println("Jogador " + jogadorDaVez + " venceu!");
                    break;
                }

                vezMaiuscula = !vezMaiuscula;
            } else {
                System.out.println("Posição fora do tabuleiro ou letra inválida. Tente outra vez.");
            }
        }
    }

    public static void escreverTabuleiro() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(tabuleiro[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void inicializarTabuleiro() {
        // Initialize the board with '-'
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiro[i][j] = '-';
            }
        }
    }

    public static boolean verificarLetras(int linha, int coluna, char letra) {
        if (linha < 0 || linha > 2 || coluna < 0 || coluna > 2) {
            return false; // Posição fora do tabuleiro
        }

        if (tabuleiro[linha][coluna] == '-') { // Espaço vazio
            if (Character.isLetter(letra)) {
                if (vezMaiuscula && Character.isUpperCase(letra) && !letrasMaiusculasUsadas.contains(letra)) {
                    tabuleiro[linha][coluna] = letra;
                    letrasMaiusculasUsadas.add(letra);
                    usadasMaiusculas += letra;
                    return true;
                } else if (!vezMaiuscula && Character.isLowerCase(letra) && !letrasMinusculasUsadas.contains(letra)) {
                    tabuleiro[linha][coluna] = letra;
                    letrasMinusculasUsadas.add(letra);
                    usadasMinusculas += letra;
                    return true;
                }
            }
        } else { // Espaço ocupado
            char letraNoTabuleiro = tabuleiro[linha][coluna];
            if (vezMaiuscula && Character.isUpperCase(letra) && Character.isLowerCase(letraNoTabuleiro) &&
                    letra > Character.toUpperCase(letraNoTabuleiro) && !letrasMaiusculasUsadas.contains(letra)) {
                tabuleiro[linha][coluna] = letra;
                letrasMaiusculasUsadas.add(letra);
                usadasMaiusculas += letra;
                return true;
            } else if (!vezMaiuscula && Character.isLowerCase(letra) && Character.isUpperCase(letraNoTabuleiro) &&
                    Character.toUpperCase(letra) > letraNoTabuleiro && !letrasMinusculasUsadas.contains(letra)) {
                tabuleiro[linha][coluna] = letra;
                letrasMinusculasUsadas.add(letra);
                usadasMinusculas += letra;
                return true;
            }
        }

        return false;
    }

    public static boolean verificarVitoria() {
        // Verificar linhas
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[i][0] != '-' && tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2]) {
                if (Character.isUpperCase(tabuleiro[i][0]) && letrasMaiusculasUsadas.contains(tabuleiro[i][0])) {
                    System.out.println("Vitória nas linhas maiúsculas!");
                    return true; // Linha com letras maiúsculas
                } else if (Character.isLowerCase(tabuleiro[i][0]) && letrasMinusculasUsadas.contains(tabuleiro[i][0])) {
                    System.out.println("Vitória nas linhas minúsculas!");
                    return true; // Linha com letras minúsculas
                }
            }
        }

        // Verificar colunas
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[0][i] != '-' && tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i]) {
                if (Character.isUpperCase(tabuleiro[0][i]) && letrasMaiusculasUsadas.contains(tabuleiro[0][i])) {
                    System.out.println("Vitória nas colunas maiúsculas!");
                    return true; // Coluna com letras maiúsculas
                } else if (Character.isLowerCase(tabuleiro[0][i]) && letrasMinusculasUsadas.contains(tabuleiro[0][i])) {
                    System.out.println("Vitória nas colunas minúsculas!");
                    return true; // Coluna com letras minúsculas
                }
            }
        }

        // Verificar diagonais
        if (tabuleiro[0][0] != '-' && tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2]) {
            if (Character.isUpperCase(tabuleiro[0][0]) && letrasMaiusculasUsadas.contains(tabuleiro[0][0])) {
                System.out.println("Vitória na diagonal principal maiúscula!");
                return true; // Diagonal principal com letras maiúsculas
            } else if (Character.isLowerCase(tabuleiro[0][0]) && letrasMinusculasUsadas.contains(tabuleiro[0][0])) {
                System.out.println("Vitória na diagonal principal minúscula!");
                return true; // Diagonal principal com letras minúsculas
            }
        }

        if (tabuleiro[0][2] != '-' && tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0]) {
            if (Character.isUpperCase(tabuleiro[0][2]) && letrasMaiusculasUsadas.contains(tabuleiro[0][2])) {
                System.out.println("Vitória na diagonal secundária maiúscula!");
                return true; // Diagonal secundária com letras maiúsculas
            } else if (Character.isLowerCase(tabuleiro[0][2]) && letrasMinusculasUsadas.contains(tabuleiro[0][2])) {
                System.out.println("Vitória na diagonal secundária minúscula!");
                return true; // Diagonal secundária com letras minúsculas
            }
        }

        return false;
    }
}
