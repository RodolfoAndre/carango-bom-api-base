package br.com.caelum.carangobom.veiculo.dashboard;

import br.com.caelum.carangobom.veiculo.Veiculo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class DashboardUtils {

    private DashboardUtils() {
    }

    /**
     * Cria o sumário de veiculos para ser utilizado no dashboard
     *
     * @param veiculos os veículos presentes
     * @return uma lista de sumários divididos por marca/modelo
     */
    public static Set<SumarioMarcaDto> criarSumarios(List<Veiculo> veiculos) {
        Set<SumarioMarcaDto> sumariosMarcaDto = new HashSet<>();
        for (Veiculo veiculo : veiculos) {
            String marcaDoVeiculo = veiculo.getMarca().getNome();
            String modeloDoVeiculo = veiculo.getModelo();

            var sumarioMarcaDto = sumariosMarcaDto.stream().filter(marca -> marca.getMarca().equals(marcaDoVeiculo)).findFirst().orElse(new SumarioMarcaDto(marcaDoVeiculo));
            sumarioMarcaDto.setValorTotal(sumarioMarcaDto.getValorTotal() + veiculo.getValor());
            sumarioMarcaDto.setNumeroDeVeiculos(sumarioMarcaDto.getNumeroDeVeiculos() + 1);

            var sumarioModeloDto = sumarioMarcaDto.getModelos().stream().filter(modelo -> modelo.getModelo().equals(modeloDoVeiculo)).findFirst().orElse(new SumarioModeloDto(modeloDoVeiculo));
            sumarioModeloDto.setValorTotal(sumarioModeloDto.getValorTotal() + veiculo.getValor());
            sumarioModeloDto.setNumeroDeVeiculos(sumarioModeloDto.getNumeroDeVeiculos() + 1);

            sumarioMarcaDto.getModelos().add(sumarioModeloDto);
            sumariosMarcaDto.add(sumarioMarcaDto);
        }

        return sumariosMarcaDto;
    }
}
