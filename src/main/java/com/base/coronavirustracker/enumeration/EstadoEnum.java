package com.base.coronavirustracker.enumeration;

public enum EstadoEnum  {

	/**
     * Nome: Acre, Sigla: AC
     */
    ACRE("Acre", "AC"),
    /**
     * Nome: Alagoas, Sigla: AL
     */
    ALAGOAS("Alagoas", "AL"),
    /**
     * Nome: Amapá, Sigla: AP
     */
    AMAPA("Amapá", "AP"),
    /**
     * Nome: Amazonas, Sigla: AM
     */
    AMAZONAS("Amazonas", "AM"),
    /**
     * Nome: Bahia, Sigla: BA
     */
    BAHIA("Bahia", "BA"),
    /**
     * Nome: Ceará, Sigla: CE
     */
    CEARA("Ceará", "CE"),
    /**
     * Nome: Distrito Federal, Sigla: DF
     */
    DISTRITOFEDERAL("Distrito Federal", "DF"),
    /**
     * Nome: Espírito Santo, Sigla: ES
     */
    ESPIRITOSANTO("Espírito Santo", "ES"),
    /**
     * Nome: Goiás, Sigla: GO
     */
    GOIAS("Goiás", "GO"),
    /**
     * Nome: Maranhão, Sigla: MA
     */
    MARANHAO("Maranhão", "MA"),
    /**
     * Nome: Mato Grosso, Sigla: MT
     */
    MATOGROSSO("Mato Grosso", "MT"),
    /**
     * Nome: Mato Grosso do Sul, Sigla: MS
     */
    MATOGROSSODOSUL("Mato Grosso do Sul", "MS"),
    /**
     * Nome: Minas Gerais, Sigla: MG
     */
    MINASGERAIS("Minas Gerais", "MG"),
    /**
     * Nome: Pará, Sigla: PA
     */
    PARA("Pará", "PA"),
    /**
     * Nome: Paraíba, Sigla: PB
     */
    PARAIBA("Paraíba", "PB"),
    /**
     * Nome: Paraná, Sigla: PR
     */
    PARANA("Paraná", "PR"),
    /**
     * Nome: Pernambuco, Sigla: PE
     */
    PERNAMBUCO("Pernambuco", "PE"),
    /**
     * Nome: Piauí, Sigla: PI
     */
    PIAUI("Piauí", "PI"),
    /**
     * Nome: Rio de Janeiro, Sigla: RJ
     */
    RIODEJANEIRO("Rio de Janeiro", "RJ"),
    /**
     * Nome: Rio Grande do Norte, Sigla: RN
     */
    RIOGRANDEDONORTE("Rio Grande do Norte", "RN"),
    /**
     * Nome: Rio Grande do Sul, Sigla: RS
     */
    RIOGRANDEDOSUL("Rio Grande do Sul", "RS"),
    /**
     * Nome Rondônia, Silga: RO
     */
    RONDONIA("Rondônia", "RO"),
    /**
     * Nome: Roraima, Sigla: RO
     */
    RORAIMA("Roraima", "RR"),
    /**
     * Nome: Santa Catarina, Sigla: SC
     */
    SANTACATARINA("Santa Catarina", "SC"),
    /**
     * Nome: São Paulo, Sigla: SP
     */
    SAOPAULO("São Paulo", "SP"),
    /**
     * Nome: Sergipe, Sigla: SE
     */
    SERGIPE("Sergipe", "SE"),
    /**
     * Nome: Tocantins, Sigla: TO
     */
    TOCANTINS("Tocantins", "TO");
    private String nome;
    private String sigla;

    
    /**
     * Construtor que recebe nome sigla
     *
     * @param nome
     * @param sigla
     */
    private EstadoEnum(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;       
        
    }

    /**
     * Retorna nome
     * 
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna sigla
     *
     * @return sigla
     */
    public String getSigla() {
        return sigla;
    }

    public static EstadoEnum fromCodigo(String sigla) {
        for (EstadoEnum enumItem : values()) {
            if (enumItem.sigla.equals(sigla)) {
                return enumItem;
            }
        }
        //
        return null;
    }
}