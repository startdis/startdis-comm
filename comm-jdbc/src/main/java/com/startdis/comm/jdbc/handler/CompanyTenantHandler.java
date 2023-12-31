package com.startdis.comm.jdbc.handler;

import com.startdis.comm.jdbc.config.TenantConfig;
import com.startdis.comm.util.auth.AuthInfoUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author Startdis
 * @email startdis@dianjiu.cc
 * @desc 集团租户ID字段填充拦截器
 */
@Slf4j
public class CompanyTenantHandler extends AbstractTenantHandler {


    public CompanyTenantHandler(TenantConfig tenantConfig) {
        super(tenantConfig.getCompanyTenant());
    }


    @Override
    protected String doGetTenantId() {
        return Optional.ofNullable(AuthInfoUtils.getCompanyTenantId()).orElse("");
    }

    @Override
    protected String getDefaultColumn() {
        return "company_tenant_id";
    }
}
