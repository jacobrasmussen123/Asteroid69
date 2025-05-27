package dk.sdu.cbse.core;

import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

@Configuration
public class ModuleConfig {
    @Bean
    public Game game(
            List<IGamePluginService> gamePlugins,
            List<IEntityProcessingService> entityProcessors,
            List<IPostEntityProcessingService> postProcessors
    ) {
        return new Game(gamePlugins, entityProcessors, postProcessors);
    }

    @Bean
    public List<IGamePluginService> gamePlugins() {
        return ServiceLoader.load(IGamePluginService.class)
                .stream().map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }

    @Bean
    public List<IEntityProcessingService> entityProcessors() {
        return ServiceLoader.load(IEntityProcessingService.class)
                .stream().map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }

    @Bean
    public List<IPostEntityProcessingService> postProcessors() {
        return ServiceLoader.load(IPostEntityProcessingService.class)
                .stream().map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }
}
