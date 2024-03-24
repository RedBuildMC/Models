package fr.redbuild.models.paper.config;

import fr.redbuild.models.paper.Autowired.Autowired;

public class ConfigManager {
    @Autowired
    private ConfigRepository configRepository;

    public Config getConfig(String name) {
        var result = configRepository.findAll().stream().filter(config -> config.getName().equals(name)).findFirst();
        if(result.isPresent()) {
            return result.get();
        }else{
            return createConfig(name);
        }
    }

    public void saveConfig(Config config) {
        configRepository.save(config);
    }

    public void deleteConfig(String name) {
        var result = configRepository.findAll().stream().filter(config -> config.getName().equals(name)).findFirst();
        if(result.isPresent()) {
            configRepository.delete(result.get());
        }  
    }

    public Config createConfig(String name) {
        Config config = new Config(name);
        configRepository.save(config);
        return config;
    }
}
