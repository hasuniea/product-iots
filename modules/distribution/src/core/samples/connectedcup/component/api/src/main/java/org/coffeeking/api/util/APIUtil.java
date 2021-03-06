/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.coffeeking.api.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.analytics.api.AnalyticsDataAPI;
import org.wso2.carbon.analytics.api.AnalyticsDataAPIUtil;
import org.wso2.carbon.analytics.dataservice.commons.AnalyticsDataResponse;
import org.wso2.carbon.analytics.dataservice.commons.SearchResultEntry;
import org.wso2.carbon.analytics.dataservice.commons.SortByField;
import org.wso2.carbon.analytics.datasource.commons.Record;
import org.wso2.carbon.analytics.datasource.commons.exception.AnalyticsException;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.device.mgt.common.authorization.DeviceAccessAuthorizationService;
import org.wso2.carbon.device.mgt.core.service.DeviceManagementProviderService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides utility functions used by REST-API.
 */
public class APIUtil {

    private static Log log = LogFactory.getLog(APIUtil.class);

    public static String getAuthenticatedUser() {
        PrivilegedCarbonContext threadLocalCarbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        String username = threadLocalCarbonContext.getUsername();
        String tenantDomain = threadLocalCarbonContext.getTenantDomain();
        if (username.endsWith(tenantDomain)) {
            return username.substring(0, username.lastIndexOf("@"));
        }
        return username;
    }

    public static DeviceManagementProviderService getDeviceManagementService() {
        PrivilegedCarbonContext ctx = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        DeviceManagementProviderService deviceManagementProviderService =
                (DeviceManagementProviderService) ctx.getOSGiService(DeviceManagementProviderService.class, null);
        if (deviceManagementProviderService == null) {
            throw new IllegalStateException("Device Management service has not initialized");
        }
        return deviceManagementProviderService;
    }

    public static DeviceAccessAuthorizationService getDeviceAccessAuthorizationService() {
        PrivilegedCarbonContext ctx = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        DeviceAccessAuthorizationService deviceAccessAuthorizationService =
                (DeviceAccessAuthorizationService) ctx.getOSGiService(DeviceAccessAuthorizationService.class, null);
        if (deviceAccessAuthorizationService == null) {
            throw new IllegalStateException("Device Authorization service has not initialized");
        }
        return deviceAccessAuthorizationService;
    }

    public static AnalyticsDataAPI getAnalyticsDataAPI() {
        PrivilegedCarbonContext ctx = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        AnalyticsDataAPI analyticsDataAPI =
                (AnalyticsDataAPI) ctx.getOSGiService(AnalyticsDataAPI.class, null);
        if (analyticsDataAPI == null) {
            throw new IllegalStateException("Analytics api service has not initialized");
        }
        return analyticsDataAPI;
    }

    public static List<SensorRecord> getAllEventsForDevice(String tableName, String query,
                                                           List<SortByField> sortByFields) throws AnalyticsException {
        int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        AnalyticsDataAPI analyticsDataAPI = getAnalyticsDataAPI();
        int eventCount = analyticsDataAPI.searchCount(tenantId, tableName, query);
        // limiting the data read from the server
        int start = 0;
        int dataCount = 100;
        if (eventCount == 0) {
            return null;
        } else if (eventCount >= dataCount){
            start = eventCount - dataCount;
        }

        List<SearchResultEntry> resultEntries = analyticsDataAPI.search(tenantId, tableName, query, start, eventCount,
                                                                        sortByFields);
        List<String> recordIds = getRecordIds(resultEntries);
        AnalyticsDataResponse response = analyticsDataAPI.get(tenantId, tableName, 1, null, recordIds);
        Map<String, SensorRecord> sensorDatas = createSensorData(AnalyticsDataAPIUtil.listRecords(
                analyticsDataAPI, response));
        List<SensorRecord> sortedSensorData = getSortedSensorData(sensorDatas, resultEntries);
        return sortedSensorData;
    }


    private static List<String> getRecordIds(List<SearchResultEntry> searchResults) {
        List<String> ids = new ArrayList<>();
        for (SearchResultEntry searchResult : searchResults) {
            ids.add(searchResult.getId());
        }
        return ids;
    }

    public static List<SensorRecord> getSortedSensorData(Map<String, SensorRecord> sensorDatas,
                                                         List<SearchResultEntry> searchResults) {
        List<SensorRecord> sortedRecords = new ArrayList<>();
        for (SearchResultEntry searchResultEntry : searchResults) {
            sortedRecords.add(sensorDatas.get(searchResultEntry.getId()));
        }
        return sortedRecords;
    }

    /**
     * Creates the SensorDatas from records.
     *
     * @param records the records
     * @return the Map of SensorRecord <id, SensorRecord>
     */
    public static Map<String, SensorRecord> createSensorData(List<Record> records) {
        Map<String, SensorRecord> sensorDatas = new HashMap<>();
        for (Record record : records) {
            SensorRecord sensorData = createSensorData(record);
            sensorDatas.put(sensorData.getId(), sensorData);
        }
        return sensorDatas;
    }

    /**
     * Create a SensorRecord object out of a Record object
     *
     * @param record the record object
     * @return SensorRecord object
     */
    public static SensorRecord createSensorData(Record record) {
        SensorRecord recordBean = new SensorRecord();
        recordBean.setId(record.getId());
        recordBean.setValues(record.getValues());
        return recordBean;
    }
}
