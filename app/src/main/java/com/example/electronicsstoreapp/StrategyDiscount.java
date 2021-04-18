package com.example.electronicsstoreapp;

public interface StrategyDiscount
{
    public double discount();

    class threepurchasehistory implements StrategyDiscount
    {
        @Override
        public double discount() {
            return 0.10;
        }
    }
    class fivepurchasehistory implements StrategyDiscount
    {
        @Override
        public double discount() {
            return 0.20;
        }
    }
    class eightpurchasehistory implements StrategyDiscount
    {
        @Override
        public double discount() {
            return 0.35;
        }
    }
    class hundredpurchasehistory implements StrategyDiscount
    {
        @Override
        public double discount() {
            return 0.50;
        }
    }


}
