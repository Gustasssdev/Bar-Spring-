    package DiogoRangel.Bar.repository;

    import DiogoRangel.Bar.classes.Consumo;
    import DiogoRangel.Bar.dto.ItemVendaDTO;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;

    import java.util.List;

    public interface ConsumoRepository extends JpaRepository<Consumo, Long> {
        // 1. Itens Mais Vendidos (Quantos foram vendidos)
        @Query("SELECT new DiogoRangel.Bar.dto.ItemVendaDTO(c.item.tipo, c.item.nome, SUM(c.quantidade)) " +
                "FROM Consumo c " +
                "WHERE c.status <> DiogoRangel.Bar.enums.StatusConsumo.CANCELADO " +
                "GROUP BY c.item.tipo, c.item.nome " +
                "ORDER BY SUM(c.quantidade) DESC")
        List<ItemVendaDTO> findItensMaisVendidos();

        // 2. Itens com Maior Faturamento (Qual o valor total de venda)
        @Query("SELECT new DiogoRangel.Bar.dto.ItemVendaDTO(c.item.tipo, c.item.nome, SUM(c.item.valor * c.quantidade)) " +
                "FROM Consumo c " +
                "WHERE c.status <> DiogoRangel.Bar.enums.StatusConsumo.CANCELADO " +
                "GROUP BY c.item.tipo, c.item.nome " +
                "ORDER BY SUM(c.item.valor * c.quantidade) DESC")
        List<ItemVendaDTO> findItensMaiorFaturamento();
    }

