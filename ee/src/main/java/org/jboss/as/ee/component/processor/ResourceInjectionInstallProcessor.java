/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.ee.component.processor;

import org.jboss.as.ee.component.Attachments;
import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.injection.ResourceInjectionConfiguration;
import org.jboss.as.ee.component.injection.ResourceInjectionResolver;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;

/**
 * @author John Bailey
 */
public class ResourceInjectionInstallProcessor extends AbstractComponentConfigProcessor {

    protected void processComponentConfig(DeploymentUnit deploymentUnit, DeploymentPhaseContext phaseContext, ComponentConfiguration componentConfiguration) throws DeploymentUnitProcessingException {
        final Class<?> componentClass = componentConfiguration.getAttachment(Attachments.COMPONENT_CLASS);
        final ResourceInjectionResolver resolver = componentConfiguration.getAttachment(Attachments.RESOURCE_INJECTION_RESOLVER);

        // Process the component's injections
        for (ResourceInjectionConfiguration resourceConfiguration : componentConfiguration.getResourceInjectionConfigs()) {
            final ResourceInjectionResolver.ResolverResult result = resolver.resolve(deploymentUnit, componentConfiguration.getName(), componentClass, resourceConfiguration);
            if (result.getInjection() != null) {
                componentConfiguration.addToAttachmentList(Attachments.RESOURCE_INJECTIONS, result.getInjection());
            }
            componentConfiguration.addToAttachmentList(Attachments.RESOLVED_RESOURCES, result);
        }
    }
}
