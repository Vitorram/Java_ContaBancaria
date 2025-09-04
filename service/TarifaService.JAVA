public class TarifaService {

    private TarifaStrategy estrategia;

    
    public TarifaService(TarifaStrategy estrategia) {
        this.estrategia = estrategia;
    }

   
    public double calcularTarifa(double valor) {
        return estrategia.aplica(valor);
    }

   
    public void setEstrategia(TarifaStrategy estrategia) {
        this.estrategia = estrategia;
    }
}