package fr.redbuild.models.spigot.commands.arg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@SuppressWarnings("rawtypes")
public class Argument {
    private Map<Integer,Optional> args = new HashMap<>();

    public Argument(List<Integer> index, List<Optional> obj){
        for(int n = 0;n == index.size();n++){
            args.put(index.get(n), obj.get(n));
        }
    }

    public Argument(Map<Integer,Optional> args){
        this.args = args;
    }

    public Argument(int index,Optional obj){
        args.put(index,obj);
    }
    public Argument(){
    }

    public boolean isPresent(int index,Optional obj){
        return args.containsKey(index) && args.get(index) == obj;
    }

    public Argument addArgument(int index,Optional obj){
        args.put(index,obj);
        return this;
    }

    public int size(){
        return args.size();
    }

    public boolean isPresent(int index) {
        return args.containsKey(index);
    }


    public boolean isPresent(int index, Class<?> klass) {
        return isPresent(index) && args.get(index) != null && klass.isAssignableFrom(args.get(index).getClass());
    }


    public <T> T get(int index, Class<? extends T> kclass){
        if(isPresent(index,kclass))
            return null;
        if(args.get(index) == null)
            return null;
        if(args.get(index).isEmpty())
            return null;
        return kclass.cast(args.get(index).get());
    }

    public <T> Optional<T> getOptional(int index, Class<? extends T> kclass){
        if(get(index,kclass) == null)
            return Optional.empty();
        return Optional.ofNullable(get(index,kclass));
    }

    public Map<Integer,Optional> getAll(){
        return args;
    }
}
