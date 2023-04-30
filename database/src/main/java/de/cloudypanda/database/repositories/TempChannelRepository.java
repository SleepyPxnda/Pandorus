package de.cloudypanda.database.repositories;

import de.cloudypanda.database.entities.TempChannelConfig;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TempChannelRepository extends CrudRepository<TempChannelConfig, Long> {
    @Transactional
    @Modifying
    @Query("update TempChannelConfig t set t.tempTriggerChannelId = ?1, t.tempChannelPrefix =?2 where t.guildId = ?3")
    int updateTempTriggerChannelIdByGuildId(Long tempTriggerChannelId, String prefix, Long guildId);
    TempChannelConfig findTempChannelConfigByGuildId(Long guildId);
}
