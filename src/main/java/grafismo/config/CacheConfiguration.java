package grafismo.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, grafismo.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, grafismo.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, grafismo.domain.User.class.getName());
            createCache(cm, grafismo.domain.Authority.class.getName());
            createCache(cm, grafismo.domain.User.class.getName() + ".authorities");
            createCache(cm, grafismo.domain.PersistentToken.class.getName());
            createCache(cm, grafismo.domain.User.class.getName() + ".persistentTokens");
            createCache(cm, grafismo.domain.Team.class.getName());
            createCache(cm, grafismo.domain.Team.class.getName() + ".stadiums");
            createCache(cm, grafismo.domain.Team.class.getName() + ".competitions");
            createCache(cm, grafismo.domain.Person.class.getName());
            createCache(cm, grafismo.domain.Player.class.getName());
            createCache(cm, grafismo.domain.Player.class.getName() + ".positions");
            createCache(cm, grafismo.domain.Player.class.getName() + ".actions");
            createCache(cm, grafismo.domain.TeamStaffMember.class.getName());
            createCache(cm, grafismo.domain.Referee.class.getName());
            createCache(cm, grafismo.domain.Referee.class.getName() + ".matches");
            createCache(cm, grafismo.domain.BroadcastStaffMember.class.getName());
            createCache(cm, grafismo.domain.Association.class.getName());
            createCache(cm, grafismo.domain.LocalAssociationRegion.class.getName());
            createCache(cm, grafismo.domain.LocalAssociationProvince.class.getName());
            createCache(cm, grafismo.domain.Stadium.class.getName());
            createCache(cm, grafismo.domain.Stadium.class.getName() + ".teams");
            createCache(cm, grafismo.domain.Shirt.class.getName());
            createCache(cm, grafismo.domain.Match.class.getName());
            createCache(cm, grafismo.domain.Match.class.getName() + ".referees");
            createCache(cm, grafismo.domain.MatchPlayer.class.getName());
            createCache(cm, grafismo.domain.MatchPlayer.class.getName() + ".callups");
            createCache(cm, grafismo.domain.MatchPlayer.class.getName() + ".lineups");
            createCache(cm, grafismo.domain.Callup.class.getName());
            createCache(cm, grafismo.domain.Callup.class.getName() + ".players");
            createCache(cm, grafismo.domain.Lineup.class.getName());
            createCache(cm, grafismo.domain.Lineup.class.getName() + ".players");
            createCache(cm, grafismo.domain.Formation.class.getName());
            createCache(cm, grafismo.domain.CompetitionPlayer.class.getName());
            createCache(cm, grafismo.domain.CompetitionPlayer.class.getName() + ".preferredPositions");
            createCache(cm, grafismo.domain.MatchStats.class.getName());
            createCache(cm, grafismo.domain.MatchStats.class.getName() + ".actions");
            createCache(cm, grafismo.domain.Action.class.getName());
            createCache(cm, grafismo.domain.Action.class.getName() + ".players");
            createCache(cm, grafismo.domain.Sponsor.class.getName());
            createCache(cm, grafismo.domain.Competition.class.getName());
            createCache(cm, grafismo.domain.Competition.class.getName() + ".teams");
            createCache(cm, grafismo.domain.Competition.class.getName() + ".children");
            createCache(cm, grafismo.domain.Matchday.class.getName());
            createCache(cm, grafismo.domain.Deduction.class.getName());
            createCache(cm, grafismo.domain.Suspension.class.getName());
            createCache(cm, grafismo.domain.Injury.class.getName());
            createCache(cm, grafismo.domain.Season.class.getName());
            createCache(cm, grafismo.domain.SystemConfiguration.class.getName());
            createCache(cm, grafismo.domain.ActionKey.class.getName());
            createCache(cm, grafismo.domain.Position.class.getName());
            createCache(cm, grafismo.domain.Position.class.getName() + ".parents");
            createCache(cm, grafismo.domain.Position.class.getName() + ".children");
            createCache(cm, grafismo.domain.Position.class.getName() + ".players");
            createCache(cm, grafismo.domain.Position.class.getName() + ".competitionPlayers");
            createCache(cm, grafismo.domain.Country.class.getName());
            createCache(cm, grafismo.domain.GraphicElementPos.class.getName());
            createCache(cm, grafismo.domain.GraphicElementPos.class.getName() + ".children");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
