package estudantes.entidades;

import professor.entidades.Fiscal;
import professor.entidades.Caixa;
import professor.entidades.Sacola;
import professor.entidades.Supermercado;

import java.util.Random;

public class Empacotador {
    private Random random = new Random();
    public int proximoCaixa = 1;

    public void agir(Caixa caixa, Fiscal fiscal) {
        adicionarProdutosAoMonte(caixa);
        ensacolarProdutos(caixa, fiscal, proximoCaixa);
    }

    private void adicionarProdutosAoMonte(Caixa caixa) {
        for (int i = 0; i < 5; i++) {
            Limpeza produtoLimpeza = new Limpeza(i, "Produto de Limpeza " + i, "Fabricante " + i, 100 + (i * 10));
            caixa.colocarProdutoNoMonte(produtoLimpeza);
        }

        // Criando instâncias de Cuidados Pessoais (Cosméticos) e adicionando ao monte
        for (int i = 0; i < 5; i++) {
            long validadeCosmetico = System.currentTimeMillis() + (i * 24 * 60 * 60 * 1000); // Exemplo de validade crescente
            Cosmetico cosmetico = new Cosmetico(i + 20, "Cosmetico " + i, "Fabricante " + (i + 20), 50 + (i * 5),
                    validadeCosmetico, "Fragrância " + i, (char) ('A' + (i % 4)));
            caixa.colocarProdutoNoMonte(cosmetico);
        }

        // Criando instâncias de Higiene e adicionando ao monte
        for (int i = 0; i < 5; i++) {
            long validadeHigiene = System.currentTimeMillis() + (i * 30 * 24 * 60 * 60 * 1000);
            Higiene higiene = new Higiene(i + 40, "Higiene " + i, "Fabricante " + (i + 40), 70 + (i * 10), validadeHigiene,
                    "Fragrância " + i);
            caixa.colocarProdutoNoMonte(higiene);
        }

        // Criando instâncias de Papelaria e adicionando ao monte
        for (int i = 0; i < 5; i++) {
            Papelaria papelaria = new Papelaria(i + 100, "Produto de Papelaria " + i, "Fabricante " + (i + 100), 200 + (i * 15));
            caixa.colocarProdutoNoMonte(papelaria);
        }

        // Criando instâncias de Eletroeletrônicos e adicionando ao monte
        for (int i = 0; i < 5; i++) {
            Eletroeletronico eletroeletronico = new Eletroeletronico(i + 80, "Eletroeletrônico " + i, "Fabricante " + (i + 80),
                    300 + (i * 25), (short) (110 + (i % 20)));
            caixa.colocarProdutoNoMonte(eletroeletronico);
        }

        // Criando instâncias de Produtos Alimentícios Não Perecíveis e adicionando ao monte
        for (int i = 0; i < 5; i++) {
            long validadeNaoPerecivel = System.currentTimeMillis() + (i * 365 * 24 * 60 * 60 * 1000);
            NaoPerecivel alimentoNaoPerecivel = new NaoPerecivel(i + 40, "Alimento Não Perecível " + i, "Fabricante " + (i + 40),
                    150 + (i * 12), validadeNaoPerecivel, "Tipo de Armazenamento " + i);
            caixa.colocarProdutoNoMonte(alimentoNaoPerecivel);
        }

        // Criando instâncias de Produtos Alimentícios Perecíveis e adicionando ao monte
        for (int i = 0; i < 5; i++) {
            long validadePerecivel = System.currentTimeMillis() + (i * 15 * 24 * 60 * 60 * 1000);
            Perecivel alimentoPerecivel = new Perecivel(i + 60, "Alimento Perecível " + i, "Fabricante " + (i + 60),
                    200 + (i * 10), validadePerecivel);
            caixa.colocarProdutoNoMonte(alimentoPerecivel);
        }
        if (proximoCaixa > 5) {
            proximoCaixa = 1;
        }
        proximoCaixa++;
    }

    private void ensacolarProdutos(Caixa caixa, Fiscal fiscal, int proximoCaixa) {
        int numeroSacola = 1;

        caixa.contarProdutosNoMonte();

        for (Produto produto : caixa.getArrayDoMonte()) {
            Produto removido = caixa.pegarProdutoDoMonte(produto);
            if (proximoCaixa > 5) {
                proximoCaixa = 1;
            }
            if (removido != null) {
                Sacola sacolaAtual = caixa.getSacola(numeroSacola);
                if (sacolaAtual != null) {
                    sacolaAtual.colocarProdutoNaSacola(removido);
                    switch (numeroSacola) {
                        case 1:
                            apenasRefrigerados(caixa, sacolaAtual, removido, fiscal);
                            break;
                        case 2:
                            apenasCuidadosPessoais(caixa, sacolaAtual, removido, fiscal);
                            break;
                        case 3:
                            apenasAlimenticios(caixa, sacolaAtual, removido, fiscal);
                            break;
                        case 4:
                            apenasLimpeza(caixa, sacolaAtual, removido, fiscal);
                            break;
                        case 5:
                            apenasEletroeletronico(caixa, sacolaAtual, removido, fiscal);
                            break;
                        default:
                            break;
                    }

                    if (calculoPeso(caixa, sacolaAtual, produto)) {
                        fiscal.despachar(sacolaAtual);
                    }
                }
                numeroSacola++;
                if (numeroSacola > 5) {
                    numeroSacola = 1;
                }
                proximoCaixa++;
            }
        }
    }

    // refrigerados só podem estar com refrigerados
    // e não podem ter temperaturas muito diferentes +- 15 graus
    private void apenasRefrigerados(Caixa caixa, Sacola sacola, Produto removido, Fiscal fiscal) {
        for (Produto produto : caixa.getArrayDoMonte()) {
            Produto refrigerado = caixa.pegarProdutoDoMonte(produto);
            if (!(refrigerado instanceof Refrigerado)) {
                caixa.colocarProdutoNoMonte(refrigerado);
            }
            if (calculoPeso(caixa, sacola, refrigerado) == true) {
                fiscal.despachar(sacola);
            }
        }
    }

    private void apenasAlimenticios(Caixa caixa, Sacola sacola, Produto removido, Fiscal fiscal) {
        for (Produto produto : caixa.getArrayDoMonte()) {
            Produto alimenticio = caixa.pegarProdutoDoMonte(produto);
            if (!(alimenticio instanceof Alimenticio)) {
                caixa.colocarProdutoNoMonte(alimenticio);
            }
            if (calculoPeso(caixa, sacola, alimenticio) == true) {
                fiscal.despachar(sacola);
            }
        }
    }

    private void apenasEletroeletronico(Caixa caixa, Sacola sacola, Produto removido, Fiscal fiscal) {
        for (Produto produto : caixa.getArrayDoMonte()) {
            Produto eletronico = caixa.pegarProdutoDoMonte(produto);
            if (!(eletronico instanceof Eletroeletronico)) {
                caixa.colocarProdutoNoMonte(eletronico);
            }
            if (calculoPeso(caixa, sacola, eletronico) == true) {
                fiscal.despachar(sacola);
            }
        }
    }

    private void apenasCuidadosPessoais(Caixa caixa, Sacola sacola, Produto removido, Fiscal fiscal) {
        for (Produto produto : caixa.getArrayDoMonte()) {
            Produto cuidadoPessoal = caixa.pegarProdutoDoMonte(produto);
            if (!(cuidadoPessoal instanceof CuidadosPessoais)) {
                caixa.colocarProdutoNoMonte(cuidadoPessoal);
            }
            if (calculoPeso(caixa, sacola, cuidadoPessoal) == true) {
                fiscal.despachar(sacola);
            }
        }
    }

    private void apenasLimpeza(Caixa caixa, Sacola sacola, Produto removido, Fiscal fiscal) {
        for (Produto produto : caixa.getArrayDoMonte()) {
            Produto limpeza = caixa.pegarProdutoDoMonte(produto);
            if (!(limpeza instanceof Limpeza)) {
                caixa.colocarProdutoNoMonte(limpeza);
            }
            if (calculoPeso(caixa, sacola, limpeza) == true) {
                fiscal.despachar(sacola);
            }
        }
    }

    private boolean sacolaAcimaDoPeso(Sacola sacola) {
        int pesoTotal = 0;
        for (Produto produto : sacola.getArrayDaSacola()) {
            pesoTotal += produto.getPeso();
        }
        if (pesoTotal >= 5000) {
            return true;
        } else {
            return false;
        }
    }

    private boolean sacolaAbaixoDoPeso(Sacola sacola) {
        int pesoTotal = 0;
        for (Produto produto : sacola.getArrayDaSacola()) {
            pesoTotal += produto.getPeso();
        }
        if (pesoTotal < 1000) {
            return true;
        } else {
            return false;
        }
    }

    private boolean calculoPeso(Caixa caixa, Sacola sacola, Produto removido) {
        while (sacolaAcimaDoPeso(sacola)) {
            sacola.pegarProdutoDaSacola(removido);
        }
        while (sacolaAbaixoDoPeso(sacola)) {
            sacola.colocarProdutoNaSacola(removido);
        }
        if (!sacolaAbaixoDoPeso(sacola) && !sacolaAcimaDoPeso(sacola)) {
            return true;
        } else {
            return false;
        }
    }
}
