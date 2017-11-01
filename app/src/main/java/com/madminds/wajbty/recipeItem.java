package com.madminds.wajbty;


class recipeItem {

    String getrName() {
        return rName;
    }

    void setrName(String rName) {
        this.rName = rName;
    }

    String getuName() {
        return uName;
    }

    void setuName(String uName) {
        this.uName = uName;
    }

    String getrImg() {
        return rImg;
    }

    void setrImg(String rImg) {
        this.rImg = rImg;
    }

    String getuImg() {
        return uImg;
    }

    void setuImg(String uImg) {
        this.uImg = uImg;
    }

    private  String rName,uName,rImg,uImg,recipe;

    public String getRecipe() {
        return recipe;
    }

    void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "recipeItem{" +
                "rName='" + rName + '\'' +
                ", uName='" + uName + '\'' +
                ", rImg='" + rImg + '\'' +
                ", uImg='" + uImg + '\'' +
                '}';
    }
}
