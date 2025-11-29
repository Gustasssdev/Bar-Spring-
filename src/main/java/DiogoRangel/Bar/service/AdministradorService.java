package DiogoRangel.Bar.service;

import DiogoRangel.Bar.model.Configuracao;
import DiogoRangel.Bar.model.Mesa;
import DiogoRangel.Bar.classes.ItemCardapio;
import DiogoRangel.Bar.repository.*;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdministradorService {

    private final ConfiguracaoRepository configRepository;
    private final MesaRepository mesaRepository;
    private final ItemCardapioRepository itemCardapioRepository;
    public AdministradorService(ConfiguracaoRepository configRepository, MesaRepository mesaRepository,
                                ItemCardapioRepository itemCardapioRepository) {
        this.configRepository = configRepository;
        this.mesaRepository = mesaRepository;
        this.itemCardapioRepository = itemCardapioRepository;
    }

    public Configuracao atualizarRegras(double couvert, double percBebida, double percComida) {
        // Usa o ID fixo 1L, buscando a configuração única
        Configuracao config = configRepository.findById(1L).orElse(new Configuracao());

        config.setPrecoCouvertPorPessoa(couvert);
        config.setPercentualGorjetaBebida(percBebida);
        config.setPercentualGorjetaComida(percComida);

        return configRepository.save(config);
    }

    public Mesa cadastrarMesa(Mesa novaMesa) {
        return mesaRepository.save(novaMesa);
    }

    public ItemCardapio cadastrarItem(ItemCardapio novoItem) {
        return itemCardapioRepository.save(novoItem);
    }

}