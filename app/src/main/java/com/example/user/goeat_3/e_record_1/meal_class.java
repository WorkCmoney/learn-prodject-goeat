package com.example.user.goeat_3.e_record_1;

public class meal_class {
    String meal;
    int count;

       public  meal_class(){
           meal="";
           count=0;
       }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }
}
