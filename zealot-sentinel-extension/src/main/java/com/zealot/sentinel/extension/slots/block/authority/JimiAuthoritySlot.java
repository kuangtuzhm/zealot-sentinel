/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zealot.sentinel.extension.slots.block.authority;

import java.util.List;
import java.util.Map;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;

/**
 * A {@link ProcessorSlot} that dedicates to {@link AuthorityRule} checking.
 *
 * @author leyou
 * @author Eric Zhao
 */
public class JimiAuthoritySlot extends AbstractLinkedProcessorSlot<DefaultNode> {

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode node, int count, Object... args)
        throws Throwable {
        checkBlackWhiteAuthority(resourceWrapper, context);
        fireEntry(context, resourceWrapper, node, count, args);
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        fireExit(context, resourceWrapper, count, args);
    }

    void checkBlackWhiteAuthority(ResourceWrapper resource, Context context) throws AuthorityException {
        Map<String, List<AuthorityRule>> authorityRules = JimiAuthorityRuleManager.getAuthorityRules();

        //如果没有数据,则是因为不进行权限验证或者读取规则出错从而跳过验证
        if (authorityRules == null || authorityRules.size() == 0) {
            return;
        }

        List<AuthorityRule> rules = authorityRules.get(resource.getName());
        //总体有数据但是该资源没数据，表示当前资源要禁用
        if (rules == null) {
        	throw new AuthorityException(context.getOrigin());
        }
        //不再进规则具体验证，都是白名单，已经通过资源限定了，因为资源的key就是app+service
//        for (AuthorityRule rule : rules) {
//            if (!JimiAuthorityRuleChecker.passCheck(rule, context)) {
//                throw new AuthorityException(context.getOrigin());
//            }
//        }
    }
}
