package br.com.dnd.service;

import br.com.dnd.model.AcaoCombate;
import br.com.dnd.model.AtaqueCorpoACorpo;
import br.com.dnd.model.EntidadeRPG;
import br.com.dnd.model.Magia;

public class BalanceadorCRService {

    // -------------------------------
    // TABELA BASE OFICIAL
    // -------------------------------

    private static final float[] CRS = {
        0f, 0.125f, 0.25f, 0.5f,
        1,2,3,4,5,6,7,8,9,10,
        11,12,13,14,15,16,17,18,19,20,
        21,22,23,24,25,26,27,28,29,30
    };

    private static final int[] HP = {
        8,10,15,25,
        40,60,80,100,120,140,160,175,195,215,
        235,250,265,280,300,315,335,350,370,395,
        425,450,500,550,600,650,700,750,800,850
    };

    private static final int[][] CA = {
        {8,11},
        {9,11},
        {10,12},
        {11,13},
        {13,15},
        {13,15},
        {13,15},
        {13,15},
        {14,16},
        {14,16},
        {14,16},
        {14,16},
        {15,17},
        {15,17},
        {15,17},
        {15,17},
        {16,18},
        {16,18},
        {16,18},
        {16,18},
        {17,20},
        {17,20},
        {17,20},
        {17,20},
        {18,21},
        {18,21},
        {18,21},
        {18,21},
        {19,22},
        {19,22},
        {19,22},
        {19,22},
        {20,99},
        {20,99}
    };

    private static final int[][] BONUS_ATAQUE = {
        {2,3},
        {3,4},
        {3,4},
        {3,4},
        {4,5},
        {4,5},
        {4,5},
        {4,5},
        {6,8},
        {6,8},
        {6,8},
        {6,8},
        {7,9},
        {7,9},
        {7,9},
        {7,9},
        {9,11},
        {9,11},
        {9,11},
        {9,11},
        {10,12},
        {10,12},
        {10,12},
        {10,12},
        {11,13},
        {11,13},
        {11,13},
        {11,13},
        {12,14},
        {12,14},
        {12,14},
        {12,14},
        {14,99},
        {14,99}
    };

    private static final int[][] DANO = {
        {0,2},
        {2,4},
        {5,7},
        {8,10},
        {11,18},
        {19,25},
        {26,31},
        {32,37},
        {38,43},
        {44,49},
        {50,55},
        {56,61},
        {62,67},
        {68,73},
        {74,79},
        {80,85},
        {86,91},
        {92,97},
        {98,103},
        {104,109},
        {110,115},
        {116,121},
        {122,139},
        {140,157},
        {158,175},
        {176,193},
        {194,211},
        {212,229},
        {230,247},
        {248,265},
        {266,283},
        {284,301},
        {302,319},
        {320,357}
    };

    private static final int[][] DC = {
        {8,11},
        {9,11},
        {10,12},
        {11,13},
        {13,15},
        {13,15},
        {13,15},
        {13,15},
        {14,16},
        {14,16},
        {14,16},
        {14,16},
        {15,17},
        {15,17},
        {15,17},
        {15,17},
        {16,18},
        {16,18},
        {16,18},
        {16,18},
        {17,19},
        {17,19},
        {17,19},
        {17,19},
        {18,21},
        {18,21},
        {18,21},
        {18,21},
        {21,99},
        {21,99},
        {22,99},
        {22,99},
        {23,99},
        {23,99}
    };

    // -------------------------------
    // CÁLCULO PRINCIPAL
    // -------------------------------

    public float calcularCRReal(EntidadeRPG monstro) {

        float crVida = descobrirCRPorHP(monstro.getHpBase());
        float crCA = descobrirCRPorCA(monstro.getCa());

        int maiorAtaque = 0;
        int maiorDano = 0;
        int maiorDC = 0;

        for (AcaoCombate acao : monstro.getAcoes()) {

            if (acao instanceof AtaqueCorpoACorpo ataque) {

                maiorAtaque = Math.max(
                    maiorAtaque,
                    ataque.getBonusAtaque()
                );

                maiorDano = Math.max(
                    maiorDano,
                    ataque.getDanoMedio()
                );
            }

            if (acao instanceof Magia magia) {

                maiorDC = Math.max(
                    maiorDC,
                    magia.getSaveDc()
                );
            }
        }

        float crAtaque = descobrirCRPorAtaque(maiorAtaque);
        float crDano = descobrirCRPorDano(maiorDano);
        float crDC = descobrirCRPorDC(maiorDC);

        float media =
            (crVida + crCA + crAtaque + crDano + crDC) / 5f;

        return arredondarCR(media);
    }

    // -------------------------------
    // REBALANCEAMENTO AUTOMÁTICO
    // -------------------------------

    public void balancearFichaParaCR(EntidadeRPG monstro) {

        float alvo = monstro.getCr();

        int indice = encontrarIndiceCR(alvo);

        if (indice == -1) return;

        monstro.setHpBase(HP[indice]);

        int caMedia =
            (CA[indice][0] + CA[indice][1]) / 2;

        monstro.setCa(caMedia);

        int bonusMedio =
            (BONUS_ATAQUE[indice][0]
            + BONUS_ATAQUE[indice][1]) / 2;

        int danoMedio =
            (DANO[indice][0]
            + DANO[indice][1]) / 2;

        for (AcaoCombate acao : monstro.getAcoes()) {

            if (acao instanceof AtaqueCorpoACorpo ataque) {

                ataque.setBonusAtaque(bonusMedio);

                ataque.setDanoBonus(danoMedio / 2);
            }

            if (acao instanceof Magia magia) {

                int dcMedia =
                    (DC[indice][0]
                    + DC[indice][1]) / 2;

                magia.setSaveDc(dcMedia);
            }
        }
    }

    // -------------------------------
    // MÉTODOS AUXILIARES
    // -------------------------------

    private float descobrirCRPorHP(int hp) {

        for (int i = 0; i < HP.length; i++) {

            if (hp <= HP[i]) {
                return CRS[i];
            }
        }

        return 30;
    }

    private float descobrirCRPorCA(int ca) {

        for (int i = 0; i < CA.length; i++) {

            if (ca >= CA[i][0]
             && ca <= CA[i][1]) {

                return CRS[i];
            }
        }

        return 30;
    }

    private float descobrirCRPorAtaque(int bonus) {

        for (int i = 0; i < BONUS_ATAQUE.length; i++) {

            if (bonus >= BONUS_ATAQUE[i][0]
             && bonus <= BONUS_ATAQUE[i][1]) {

                return CRS[i];
            }
        }

        return 30;
    }

    private float descobrirCRPorDano(int dano) {

        for (int i = 0; i < DANO.length; i++) {

            if (dano >= DANO[i][0]
             && dano <= DANO[i][1]) {

                return CRS[i];
            }
        }

        return 30;
    }

    private float descobrirCRPorDC(int dc) {

        for (int i = 0; i < DC.length; i++) {

            if (dc >= DC[i][0]
             && dc <= DC[i][1]) {

                return CRS[i];
            }
        }

        return 30;
    }

    private int encontrarIndiceCR(float cr) {

        for (int i = 0; i < CRS.length; i++) {

            if (CRS[i] == cr) {
                return i;
            }
        }

        return -1;
    }

    private float arredondarCR(float valor) {

        return Math.round(valor);
    }

    public int[] obterStatusBasePorCR(float cr) {
        int indice = encontrarIndiceCR(cr);
        
        // Se digitar um CR que não existe, retorna nulo para a tela tratar
        if (indice == -1) return null;

        int hpMedio = HP[indice];
        int caMedio = (CA[indice][0] + CA[indice][1]) / 2;
        int bonusAtaqueMedio = (BONUS_ATAQUE[indice][0] + BONUS_ATAQUE[indice][1]) / 2;

        // Bônus de Proficiência padrão de D&D 5e: (CR / 4) + 1 (arredondado para baixo, mínimo 2)
        int proficiencia = Math.max(2, ((int) cr / 4) + 1);
        
        // Descobrindo qual deveria ser o Modificador do Atributo principal do monstro
        int modAtributo = bonusAtaqueMedio - proficiencia;
        
        // Convertendo o modificador no valor do atributo (ex: mod +3 = atributo 16)
        int valorAtributoBase = 10 + (modAtributo * 2);

        // Retorna: [0] = HP, [1] = CA, [2] = Atributo Principal Alto
        return new int[]{hpMedio, caMedio, valorAtributoBase};
    }
}