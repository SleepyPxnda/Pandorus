package de.cloudypanda.database.repositories;

import de.cloudypanda.database.entities.TempChannelConfig;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TempChannelRepository extends CrudRepository<TempChannelConfig, Long> {
    @Transactional
    @Modifying
    @Query("update TempChannelConfig t set t.tempTriggerChannelId = ?1 where t.guildId = ?2")
    int updateTempTriggerChannelIdByGuildId(Long tempTriggerChannelId, Long guildId);
    TempChannelConfig findTempChannelConfigByGuildId(Long guildId);
}
