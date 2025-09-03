package app;

import model.ContaCorrente;
import service.ContaService;
import exception.SaldoInsuficienteException;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ContaService cs = new ContaService();

        try {
            // Lê todas as contas do arquivo
            List<ContaCorrente> todasContas = cs.lerConta("conta.txt");

            // Filtra contas com saldo > 1000 apenas para exibição
            List<ContaCorrente> contasFiltradas = cs.contas1000("conta.txt");

            // Monta string para exibir contas filtradas
            StringBuilder sb = new StringBuilder();
            contasFiltradas.forEach(c -> sb.append(
                    "Número: " + c.getNumero() +
                    " | Titular: " + c.getTitular() +
                    " | Saldo: R$ " + String.format("%.2f", c.getSaldo()) + "\n"
            ));
        
            //Mostrar todas as contas
            StringBuilder sbTodas = new StringBuilder();
            todasContas.forEach(c -> sbTodas.append(
                "Número: " + c.getNumero() +
                " | Titular: " + c.getTitular() +
                " | Saldo: R$ " + String.format("%.2f", c.getSaldo()) + "\n"
            ));


            JOptionPane.showMessageDialog(null, sbTodas.toString(), "Todas as contas", JOptionPane.INFORMATION_MESSAGE);
            //seleciona a conta pelo num
            String numeroStr = JOptionPane.showInputDialog("Informe o número da conta que deseja acessar:");
            int numeroConta = Integer.parseInt(numeroStr);
            ContaCorrente contaSelect = todasContas.stream()
                                        .filter(c->c.getNumero() == numeroConta)
                                        .findFirst()
                                        .orElse(null);
            if (contaSelect != null){
            JOptionPane.showMessageDialog(null,
            "Conta encontrada!\nNúmero: " + contaSelect.getNumero() +
            "\nTitular: " + contaSelect.getTitular() +
            "\nSaldo: R$ " + String.format("%.2f", contaSelect.getSaldo())
             );
            }
            else{
                JOptionPane.showMessageDialog(null, "Erro felas, nao tem conta com esse numero");
                return;
            }



            // Solicita valor para saque
            String valorStr = JOptionPane.showInputDialog("Informe o valor para saque:");
            double valor = Double.parseDouble(valorStr);


            try {
                // Realiza saque em todas as contas
                
                cs.sacarValor(List.of(contaSelect), valor);
                JOptionPane.showMessageDialog(null, "Saque realizado com sucesso!");
            } catch (SaldoInsuficienteException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }

            // Atualiza arquivo com todas as contas
            cs.atualizarConta(List.of(contaSelect), "conta_atualizada.txt");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao acessar arquivo: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
