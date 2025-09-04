package app;

import model.ContaCorrente;
import service.ContaService;
import exception.SaldoInsuficienteException;
import strategy.TarifaStrategy;  

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        ContaService cs = new ContaService();

        try {
            // le todas as contas do arquivo
            List<ContaCorrente> todasContas = cs.lerConta("conta.txt");

            // mostra todas as contas
            StringBuilder sbTodas = new StringBuilder();
            todasContas.forEach(c -> sbTodas.append(
                    "Número: " + c.getNumero() +
                    " | Titular: " + c.getTitular() +
                    " | Saldo: R$ " + String.format("%.2f", c.getSaldo()) + "\n"
            ));
            JOptionPane.showMessageDialog(null, sbTodas.toString(), "Todas as contas", JOptionPane.INFORMATION_MESSAGE);

            // contas com saldo maior que 1000
            List<ContaCorrente> contasFiltradas = cs.contas1000("conta.txt");
            StringBuilder sbFiltradas = new StringBuilder();
            contasFiltradas.forEach(c -> sbFiltradas.append(
                    "Número: " + c.getNumero() +
                    " | Titular: " + c.getTitular() +
                    " | Saldo: R$ " + String.format("%.2f", c.getSaldo()) + "\n"
            ));
            System.out.println("Contas com saldo maior que 1000:");
            System.out.println(sbFiltradas.toString());
            
            // conta com maior saldo
            Optional<ContaCorrente> maiorDelas = cs.maiorConta("conta.txt");
            StringBuilder sbMaior = new StringBuilder();
            maiorDelas.ifPresent(c -> sbMaior.append(
                    "Número: " + c.getNumero() +
                    " | Titular: " + c.getTitular() +
                    " | Saldo: R$ " + String.format("%.2f", c.getSaldo()) + "\n"
            ));
            
            // contas com saldo maior que 5000
            List<ContaCorrente> conta5 = cs.contas5000("conta.txt");
            StringBuilder sb5000 = new StringBuilder();

            conta5.forEach(c -> sb5000.append(
                "Número: " + c.getNumero() +
                " | Titular: " + c.getTitular() +
                " | Saldo: R$ " + String.format("%.2f", c.getSaldo()) + "\n"
            ));
            System.out.println("Contas com saldo > 5000:");
            System.out.println(sb5000.toString());  
            
            // contas com número par
            List<ContaCorrente> contaPar = cs.contasPar("conta.txt");
            StringBuilder sbPar = new StringBuilder();
            contaPar.forEach(c -> sbPar.append(
                "Número: " + c.getNumero() +
                " | Titular: " + c.getTitular() +
                " | Saldo: R$ " + String.format("%.2f", c.getSaldo()) + "\n"
            ));
            System.out.println("Contas com número par:");
            System.out.println(sbPar.toString());

            // contas com saldo decrescente
            List<ContaCorrente> contaOrd = cs.compararN("conta.txt");
            StringBuilder sbORd = new StringBuilder();
            contaOrd.forEach(c -> sbORd.append(
                "Número: " + c.getNumero() +
                " | Titular: " + c.getTitular() +
                " | Saldo: R$ " + String.format("%.2f", c.getSaldo()) + "\n"
            ));
            System.out.println("Contas com saldo decrescente:");
            System.out.println(sbORd.toString());

            // contas com nome A - Z
            List<ContaCorrente> contaOrdNome = cs.compararT("conta.txt");
            StringBuilder sbORdNome = new StringBuilder();
            contaOrdNome.forEach(c -> sbORdNome.append(
                "Número: " + c.getNumero() +
                " | Titular: " + c.getTitular() +
                " | Saldo: R$ " + String.format("%.2f", c.getSaldo()) + "\n"
            ));
            System.out.println("Contas com nome A - Z:");
            System.out.println(sbORdNome.toString());

            // seleciona a conta pelo número
            String numeroStr = JOptionPane.showInputDialog("Informe o número da conta que deseja acessar:");
            int numeroConta = Integer.parseInt(numeroStr);
            ContaCorrente contaSelect = todasContas.stream()
                    .filter(c -> c.getNumero() == numeroConta)
                    .findFirst()
                    .orElse(null);

            if (contaSelect != null) {
                JOptionPane.showMessageDialog(null,
                        "Conta encontrada!\nNúmero: " + contaSelect.getNumero() +
                        "\nTitular: " + contaSelect.getTitular() +
                        "\nSaldo: R$ " + String.format("%.2f", contaSelect.getSaldo())
                );
            } else {
                JOptionPane.showMessageDialog(null, "Erro, não existe conta com esse número");
                return;
            }

            // aplica a tarifa
            String[] opcoesTarifa = {"FIXA", "PERCENTUAL", "ISENTA"};
            String escolha = (String) JOptionPane.showInputDialog(
                    null,
                    "Escolha o tipo de tarifa a aplicar:",
                    "Tarifa",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcoesTarifa,
                    "FIXA"
            );


            if (escolha != null) {
                TarifaStrategy tarifaEscolhida = TarifaStrategy.valueOf(escolha);
                double tarifa = tarifaEscolhida.aplica(contaSelect.getSaldo());
                JOptionPane.showMessageDialog(null, 
                    "Tarifa aplicada (" + escolha + "): R$ " + String.format("%.2f", tarifa));
                contaSelect.setSaldo(contaSelect.getSaldo() - tarifa);
                JOptionPane.showMessageDialog(null,
                    "Novo saldo após a tarifa: R$ " + String.format("%.2f", contaSelect.getSaldo()));
            }

         

            String[] opcoes = {"Sacar", "Depositar"};
            String escolhaOperacao = (String) JOptionPane.showInputDialog(
                null,
                "Escolha a operação desejada:",
                "Operações",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                "Sacar");

            if (escolhaOperacao != null) {
                if (escolhaOperacao.equals("Sacar")) {
                    // Solicita valor para saque
                    String valorStr1 = JOptionPane.showInputDialog("Informe o valor para saque:");
                    double valor11 = Double.parseDouble(valorStr1);

                    try {
                        cs.sacarValor(contaSelect, valor11); // Realiza o saque na conta selecionada
                        JOptionPane.showMessageDialog(null, "Saque realizado com sucesso!");
                    } catch (SaldoInsuficienteException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }

                } else if (escolhaOperacao.equals("Depositar")) {
                    // Solicita valor para depósito
                    String valorDPR = JOptionPane.showInputDialog("Informe o valor para depósito:");
                    double valor1 = Double.parseDouble(valorDPR);

                    // Realiza o depósito
                    cs.depositar(contaSelect, valor1);
                    JOptionPane.showMessageDialog(null, "Depósito realizado com sucesso!");
                }
            }

            




            // atualiza arquivo com todas as contas
            cs.atualizarConta(todasContas, "conta_atualizada.txt");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao acessar arquivo: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
