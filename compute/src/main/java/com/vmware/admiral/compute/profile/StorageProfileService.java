/*
 * Copyright (c) 2016 VMware, Inc. All Rights Reserved.
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with separate copyright notices
 * and license terms. Your use of these subcomponents is subject to the terms and
 * conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package com.vmware.admiral.compute.profile;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vmware.admiral.common.ManagementUriParts;
import com.vmware.admiral.service.common.MultiTenantDocument;
import com.vmware.xenon.common.Operation;
import com.vmware.xenon.common.ServiceDocument;
import com.vmware.xenon.common.StatefulService;
import com.vmware.xenon.common.Utils;

/**
 * Endpoint storage profile.
 */
public class StorageProfileService extends StatefulService {
    public static final String FACTORY_LINK = ManagementUriParts.STORAGE_PROFILES;

    public static class StorageItem {

        /**
         * Name of the storage properties defined for Jason's reference.
         */
        public String name;
        /**
         * Tags to be specified in the blueprint against each disk. To
         *  be used in filtering which storage item is to be used
         */
        public Set<String> tagLinks;
        /**
         * Map of storage properties that
         * are to be used by the provider when provisioning disks
         */
        public Map<String, String> diskProperties;
        /**
         * defines if this particular storage item contains default
         * storage properties
         */
        public boolean defaultItem;
    }

    public static class StorageProfile extends MultiTenantDocument {

        @Documentation(description = "Contains storageItems that define disk properties to be "
                + "used by providers")
        public List<StorageItem> storageItems;
    }

    public StorageProfileService() {
        super(StorageProfile.class);
        super.toggleOption(ServiceOption.PERSISTENCE, true);
        super.toggleOption(ServiceOption.REPLICATION, true);
        super.toggleOption(ServiceOption.OWNER_SELECTION, true);
        super.toggleOption(ServiceOption.IDEMPOTENT_POST, true);
    }

    @Override
    public void handleCreate(Operation post) {
        processInput(post);
        post.complete();
    }

    @Override
    public void handlePut(Operation put) {
        if (put.hasPragmaDirective(Operation.PRAGMA_DIRECTIVE_POST_TO_PUT)) {
            logFine("Ignoring converted PUT.");
            put.complete();
            return;
        }

        StorageProfile newState = processInput(put);
        setState(put, newState);
        put.complete();
    }

    @Override
    public void handlePatch(Operation patch) {
        StorageProfile currentState = getState(patch);
        try {
            Utils.mergeWithStateAdvanced(getStateDescription(), currentState,
                    StorageProfile.class, patch);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            patch.fail(e);
            return;
        }
        patch.setBody(currentState);
        patch.complete();
    }

    @Override
    public ServiceDocument getDocumentTemplate() {
        ServiceDocument template = super.getDocumentTemplate();
        com.vmware.photon.controller.model.ServiceUtils.setRetentionLimit(template);
        return template;
    }

    private StorageProfile processInput(Operation op) {
        if (!op.hasBody()) {
            throw (new IllegalArgumentException("body is required"));
        }
        StorageProfile state = op.getBody(StorageProfile.class);
        Utils.validateState(getStateDescription(), state);
        return state;
    }
}