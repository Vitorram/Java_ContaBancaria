package strategy;

public enum TarifaStrategy {
    //FIXA – aplica R$ 10,00 fixo; (b) PERCENTUAL –aplica 1% do saldo; (c) ISENTA – não aplica tarifa

    FIXA{
        @Override
        public double aplica(double valor) {return 10.0; } 
    },
    PERCENTUAL {
        @Override
        public double aplica(double valor) {return valor * 0.01; }
    },
    ISENTA {
        @Override
        public double aplica(double valor) {return 0.0; }
    };
     public abstract double aplica(double valor);
}
