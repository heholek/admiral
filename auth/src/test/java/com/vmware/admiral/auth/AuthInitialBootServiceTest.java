/*
 * Copyright (c) 2017 VMware, Inc. All Rights Reserved.
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with separate copyright notices
 * and license terms. Your use of these subcomponents is subject to the terms and
 * conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package com.vmware.admiral.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.GeneralSecurityException;

import org.junit.Before;
import org.junit.Test;

import com.vmware.admiral.auth.project.ProjectService;
import com.vmware.admiral.auth.project.ProjectService.ProjectState;

public class AuthInitialBootServiceTest extends AuthBaseTest {

    @Before
    public void setup() throws GeneralSecurityException {
        host.assumeIdentity(buildUserServicePath(USER_EMAIL_ADMIN));
    }

    @Test
    public void testDefaultProjectCreatedOnStartUp() throws Throwable {
        waitForServiceAvailability(ProjectService.DEFAULT_PROJECT_LINK);

        ProjectState project = getDocument(ProjectState.class,
                ProjectService.DEFAULT_PROJECT_LINK);

        assertNotNull(project);
        assertEquals(ProjectService.DEFAULT_PROJECT_ID, project.name);
        assertEquals(ProjectService.DEFAULT_PROJECT_ID, project.id);
    }
}