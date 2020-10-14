package android.rs.ac.parking;

import org.json.JSONObject;

public class PrognozaModel {
    private String mesto;
    private double temperatura;
    private double vlaznost;
    private double pritisak;

    public PrognozaModel() {
    }

    public PrognozaModel(String mesto, double temperatura, double vlaznost, double pritisak) {
        this.mesto = mesto;
        this.temperatura = temperatura;
        this.vlaznost = vlaznost;
        this.pritisak = pritisak;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getVlaznost() {
        return vlaznost;
    }

    public void setVlaznost(double vlaznost) {
        this.vlaznost = vlaznost;
    }

    public double getPritisak() {
        return pritisak;
    }

    public void setPritisak(double pritisak) {
        this.pritisak = pritisak;
    }

    public static PrognozaModel parseJSONObject(JSONObject object){
        PrognozaModel prognozaModel = new PrognozaModel();

        try{
            if(object.has("name")){
                prognozaModel.setMesto(object.getString("name"));
            }
            if(object.has("main")){
                JSONObject mainObject = object.getJSONObject("main");
                if(mainObject.has("temp")){
                    prognozaModel.setTemperatura(mainObject.getDouble("temp"));
                }
                if(mainObject.has("humidity")){
                    prognozaModel.setVlaznost(mainObject.getDouble("humidity"));
                }
                if(mainObject.has("pressure")){
                    prognozaModel.setPritisak(mainObject.getDouble("pressure"));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return prognozaModel;
    }

}
