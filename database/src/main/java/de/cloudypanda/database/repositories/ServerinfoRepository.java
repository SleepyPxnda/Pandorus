package de.cloudypanda.database.repositories;

import de.cloudypanda.database.entities.ServerInfoConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerinfoRepository extends CrudRepository<ServerInfoConfig, Long> {
    List<ServerInfoConfig> findServerInfoConfigsByGuildId(Long guildId);
    ServerInfoConfig findServerInfoConfigByServerName(String serverName);

}
