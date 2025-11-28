package game.blackjack.players;

import java.util.Random;

public enum CompPersonalities {
    TIMMY(16, new int[]{500,200,100}),   
    JIMMY(12, new int[]{100,200,500}),  
    DIMMY(14, new int[]{200,100,500}),  
    LIMMY(-1, new int[]{200,500,100}),    
    STIMMY(15, new int[]{500,500,200}),  
    BIMMY(11, new int[]{100,100,200}),   
    HIMMY(13, new int[]{200,200,100}),    
    PIMMY(-1, new int[]{100,500,200}),    
    RYAN(14, new int[]{200,100,100});  

    private final int hitThreshold;
    private final int[] betWeights; 

    CompPersonalities(int hitThreshold, int[] betWeights) {
        this.hitThreshold = hitThreshold;
        this.betWeights = betWeights;
    }

    public boolean shouldHit(int currentScore, Random rnd) {
        if (hitThreshold >= 0) {
            return currentScore < hitThreshold;
        }
        double base = 0.6 - (currentScore / 100.0);
        return rnd.nextDouble() < Math.max(0.1, base);
    }

    public int chooseBet(Random rnd) {
        int[] bets = new int[]{100,200,500};
        int total = 0;
        for (int w : betWeights) total += w;
        int pick = rnd.nextInt(total);
        int cumulative = 0;
        for (int i = 0; i < betWeights.length; i++) {
            cumulative += betWeights[i];
            if (pick < cumulative) return bets[i];
        }
        return 100;
    }

}
