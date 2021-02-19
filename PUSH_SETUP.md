## Instructions for setting push notifications 

### Default config

- IMSOrg : 'AEM Assests Departmental Campaign'
- Sandbox : 'CJM end 2 end testing'


### 1. Working in a different Sandbox in IMSOrg 'AEM Assets Departmental Campaign'

#### 1.1 Make sure correct AEP Datasets and HTTP source inlets are configured 

- check [PushConstants.java](./Sample-App/app/src/main/java/com/adobe/marketing/mobile/sampleapp/PushConstants.java)


#### 1.2 Make sure a "brand preset" is present in the sandbox

- The sandbox should have a preset with the following mobile values

"appId": "com.adobe.marketing.mobile.sampleapp",

"appPlatform": "fcm",

"name": "Android AEP SampleApp"

- Contact archanac@adobe.com to create the new preset for you.

### 2. Working in a different IMSOrg and Sandbox 

#### 2.1 Make sure correct AEP Datasets and HTTP source inlets are configured 

- check [PushConstants.java](./Sample-App/app/src/main/java/com/adobe/marketing/mobile/sampleapp/PushConstants.java)


#### 2.2 Make sure a "brand preset" is present in the sandbox

- The sandbox should have a preset with the following mobile values

"appId": "com.adobe.marketing.mobile.sampleapp",

"appPlatform": "fcm",

"name": "Android AEP SampleApp"

- Contact archanac@adobe.com to create the new preset for you.

#### 2.3 Make sure a Launch property is setup

- Create a new Edge Configuration
- Select your sandbox, experience event and profile dataset in the edge configuration
- Create a mobile property
- Add 'Mobile Core' and 'AEP Edge' extensions
- Select your edge configuration in the config for 'AEP Edge'
- Publish this mobile property
- Update the Launch Property ID in the constant "LAUNCH_ENVIRONMENT_FILE_ID" in [MainApp.java](./Sample-App/app/src/main/java/com/adobe/marketing/mobile/sampleapp/MainApp.java)


#### 2.4 Make sure a Launch App-Configuration is setup

- Ask archanac@adobe.com for the FCMV1 key
- Create a new 'App-Config' with this FCMV1 key
- Use package name as "com.adobe.marketing.mobile.sampleapp"
