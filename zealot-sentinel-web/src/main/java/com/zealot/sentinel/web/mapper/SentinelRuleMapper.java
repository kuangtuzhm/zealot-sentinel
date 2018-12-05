/*
 * COPYRIGHT. ShenZhen JiMi Technology Co., Ltd. 2017.
 * ALL RIGHTS RESERVED.
 *
 * No part of this publication may be reproduced, stored in a retrieval system, or transmitted,
 * on any form or by any means, electronic, mechanical, photocopying, recording, 
 * or otherwise, without the prior written permission of ShenZhen JiMi Network Technology Co., Ltd.
 *
 * Amendment History:
 * 
 * Date                   By              Description
 * -------------------    -----------     -------------------------------------------
 * 2017年10月12日    Pan Juncai         Create the class
 * http://www.jimilab.com/
*/

package com.zealot.sentinel.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zealot.sentinel.web.entity.SentinelRuleInfo;
/**
 * @FileName ADExtendMapper.java
 * @Description: 广告
 *
 * @Date 2017年11月30日 上午11:50:18
 * @author zhm
 * @version 1.0
 */
public interface SentinelRuleMapper {
	
	public List<SentinelRuleInfo> findByGroupId(@Param("groupId")String groupId);
}
