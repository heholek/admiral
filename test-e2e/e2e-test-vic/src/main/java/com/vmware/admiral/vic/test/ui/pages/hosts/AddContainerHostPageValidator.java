/*
 * Copyright (c) 2018-2020 VMware, Inc. All Rights Reserved.
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with separate copyright notices
 * and license terms. Your use of these subcomponents is subject to the terms and
 * conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package com.vmware.admiral.vic.test.ui.pages.hosts;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import com.vmware.admiral.test.ui.pages.clusters.AddClusterPageLocators;
import com.vmware.admiral.test.ui.pages.clusters.AddClusterPageValidator;

public class AddContainerHostPageValidator extends AddClusterPageValidator {

    public AddContainerHostPageValidator(By[] iFrameLocators,
            AddClusterPageLocators pageLocators) {
        super(iFrameLocators, pageLocators);
    }

    @Override
    public void validateIsCurrentPage() {
        element(locators().pageTitle()).shouldHave(Condition.exactText("New Container Host"));
    }

}
