package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities;

import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbutils.DbHelperUtil;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import io.vertx.core.json.JsonArray;

@Table("users")
public class AJEntityUser extends Model {
    private static final String TENANT = "tenant_id";
    private static final String TENANT_ROOT = "tenant_root";

    private static final String COLLABORATOR_VALIDATION_QUERY =
        "select tenant_id, tenant_root from users where id = ANY(?::uuid[])";

    public String getTenant() {
        return this.getString(TENANT);
    }

    public String getTenantRoot() {
        return this.getString(TENANT_ROOT);
    }

    public static LazyList<AJEntityUser> getCollaboratorsTenantInfo(JsonArray collaborators) {
        return AJEntityUser.findBySQL(COLLABORATOR_VALIDATION_QUERY,
            DbHelperUtil.toPostgresArrayString(collaborators.getList()));
    }
}
