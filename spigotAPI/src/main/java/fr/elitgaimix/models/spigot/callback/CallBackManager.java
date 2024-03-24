package fr.elitgaimix.models.spigot.callback;

import java.util.ArrayList;
import java.util.List;

public class CallBackManager {
    private List<CallBack> callBacks = new ArrayList<>();

    public void registerCallBack(CallBack callBack){
        callBacks.add(callBack);
    }

    public void deRegisterCallBack(CallBack callBack){
        callBacks.remove(callBack);
    }

    
}
