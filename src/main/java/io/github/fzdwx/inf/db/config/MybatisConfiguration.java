package io.github.fzdwx.inf.db.config;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.intf.IRelation;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.functions.IExecutor;
import cn.org.atool.fluent.mybatis.functions.TableDynamic;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.spring.IMapperFactory;
import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import cn.org.atool.fluent.mybatis.typehandler.IConvertor;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import lombok.Getter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotEmpty;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 12:34
 */
@MapperScan("io.github.fzdwx.*")
@Configuration
public class MybatisConfiguration {

    /**
     * 定义mybatis的SqlSessionFactoryBean
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean;
    }

    @Bean
    @Primary
    public MapperFactory mapperFactory() {
        return new MyMapperFactory();
    }

    static class MyMapperFactory extends MapperFactory {

        private ApplicationContext context;

        @Getter
        private static boolean isInitialized = false;

        @Getter
        private Collection<SqlSessionFactory> sessionFactories;

        @Getter
        private final List<IExecutor> initializers = new ArrayList<>();

        @Autowired
        public void setApplicationContext(ApplicationContext context) throws BeansException {
            this.context = context;
            this.sessionFactories = context.getBeansOfType(SqlSessionFactory.class).values();
            assertNotEmpty("SqlSessionFactory", this.sessionFactories);
            if (MyMapperFactory.isInitialized) {
                return;
            }
            this.init();

            MyMapperFactory.isInitialized = true;
        }

        @Override
        public Set<IEntityMapper> getMappers() {
            Map<String, IEntityMapper> mappers = context.getBeansOfType(IEntityMapper.class);
            return new HashSet<>(mappers.values());
        }

        @Override
        public Set<IRelation> getRelations() {
            Map<String, IRelation> relations = context.getBeansOfType(IRelation.class);
            return new HashSet<>(relations.values());
        }

        /**
         * 修改实体类对应的数据库类型
         *
         * @param dbType   DbType
         * @param eClasses if empty 修改所有实体对应数据库类型
         * @return ignore
         */
        public MapperFactory dbType(DbType dbType, Class<? extends IEntity>... eClasses) {
            return this.initializer(() -> RefKit.dbType(dbType, eClasses));
        }

        /**
         * 更改实体对应的数据库表名称
         *
         * @param tableSupplier 表名称更改器
         * @param eClasses      if empty, 修改所有实体对应的表名称
         * @return ignore
         */
        public MapperFactory tableSupplier(TableDynamic tableSupplier, Class<? extends IEntity>... eClasses) {
            return this.initializer(() -> RefKit.tableSupplier(tableSupplier, eClasses));
        }

        /**
         * 注册PoJoHelper.toPoJo时特定类型的转换器
         *
         * @param type      类型
         * @param convertor 类型转换器
         */
        public MapperFactory register(Type type, IConvertor convertor) {
            return this.initializer(() -> RefKit.register(type, convertor));
        }

        /**
         * 注册PoJoHelper.toPoJo时特定类型的转换器
         *
         * @param type      类型
         * @param convertor 类型转换器
         */
        public MapperFactory register(String type, IConvertor convertor) {
            return this.initializer(() -> RefKit.register(type, convertor));
        }

        /**
         * 增加环境设置脚本
         *
         * @param initializer 环境设置
         * @return ignore
         */
        public MapperFactory initializer(IExecutor initializer) {
            this.initializers.add(initializer);
            return this;
        }
    }
}