//professor, com todo respeito, mas que negócio diabólico!
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Time {

    String nome;
    int pontos;
    int golsFeitos;
    int golsSofridos;

    Time(String nomeDado) {
        nome = nomeDado;
        pontos = 0;
        golsFeitos = 0;
        golsSofridos = 0;
    }

    void addPontos(int quant) {
        pontos += quant;
    }

    void addGols(int feitos, int sofridos) {
        golsFeitos += feitos;
        golsSofridos += sofridos;
    }

    int getSaldo() {
        return golsFeitos - golsSofridos;
    }
}

public class Main {


    static Time buscarOuCriar(ArrayList<Time> tabela, String nome) {
        for (Time t : tabela) {
            if (t.nome.equals(nome)) {
                return t;
            }
        }
        Time novo = new Time(nome);
        tabela.add(novo);
        return novo;
    }


    static void ordenar(ArrayList<Time> tabela) {
        for (int i = 0; i < tabela.size() - 1; i++) {
            for (int j = 0; j < tabela.size() - 1 - i; j++) {
                Time atual = tabela.get(j);
                Time proximo = tabela.get(j + 1);

                boolean trocar = false;

                if (atual.pontos < proximo.pontos) {
                    trocar = true;
                } else if (atual.pontos == proximo.pontos) {
                    if (atual.getSaldo() < proximo.getSaldo()) {
                        trocar = true;
                    }
                }

                if (trocar) {
                    tabela.set(j, proximo);
                    tabela.set(j + 1, atual);
                }
            }
        }
    }

    public static void main(String[] args) {

        ArrayList<Time> tabela = new ArrayList<>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader("jogos.txt"));
            String linha;

            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] partes = linha.split(",");
                if (partes.length < 4) continue;

                String nomeA = partes[1].trim().replace("_", " ");
                String nomeB = partes[2].trim().replace("_", " ");
                String result = partes[3].trim();


                if (!result.contains("x") || result.startsWith("x") || result.endsWith("x")) continue;

                String[] placar = result.split("x");

                int golsA, golsB;
                try {
                    golsA = Integer.parseInt(placar[0].trim());
                    golsB = Integer.parseInt(placar[1].trim());
                } catch (NumberFormatException e) {
                    continue;
                }

                Time timeA = buscarOuCriar(tabela, nomeA);
                Time timeB = buscarOuCriar(tabela, nomeB);

                timeA.addGols(golsA, golsB);
                timeB.addGols(golsB, golsA);

                if (golsA > golsB) {
                    timeA.addPontos(3);
                } else if (golsB > golsA) {
                    timeB.addPontos(3);
                } else {
                    timeA.addPontos(1);
                    timeB.addPontos(1);
                }
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Erro ao fechar o arquivo: " + e.getMessage());
                }
            }
        }

        ordenar(tabela);


        System.out.println("+----+---------------------------+--------+-------+");
        System.out.println("| #  | Time                      | Pontos | Saldo |");
        System.out.println("+----+---------------------------+--------+-------+");


        for (int i = 0; i < tabela.size(); i++) {
            Time t = tabela.get(i);
            int posicao = i + 1;

            System.out.println("| " + posicao + "  | " + t.nome + " | " + t.pontos + " | " + t.getSaldo() + " |");
        }

        System.out.println("+----+---------------------------+--------+-------+");
    }
}