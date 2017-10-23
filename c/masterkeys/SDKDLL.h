// SDKDll.h: SDKDll DLL main header file
//

#pragma once

//#ifdef _WIN64
//	#pragma comment(lib,".\\lib\\x64\\SDKDLL.lib") 
//#else
//	#pragma comment(lib,".\\lib\\x86\\SDKDLL.lib") 
//#endif

//#pragma comment(lib,".\\SDKDLL.lib") 

#ifdef _WIN64
	#pragma comment(lib,".\\SDKDLL64.lib") 
#else
	#pragma comment(lib,".\\SDKDLL.lib") 
#endif


// LED Matrix SIZE
#define MAX_LED_ROW		 6
#define MAX_LED_COLUMN	 22



typedef struct {
	//KEY_COLOR(BYTE _r, BYTE _g, BYTE _b): r(_r), g(_g), b(_b) {};
	//KEY_COLOR() {};

	BYTE r;
	BYTE g;
	BYTE b;
} KEY_COLOR;

//  set up/save the whole LED color structure
typedef struct {
	//COLOR_MATRIX() {};

	KEY_COLOR KeyColor[MAX_LED_ROW][MAX_LED_COLUMN];
} COLOR_MATRIX;

typedef void (CALLBACK * KEY_CALLBACK)(int iRow, int iColumn, bool bPressed);


//Enumeration of effects
typedef enum { EFF_FULL_ON = 0, EFF_BREATH = 1, EFF_BREATH_CYCLE = 2 , EFF_SINGLE = 3,  EFF_WAVE = 4, EFF_RIPPLE = 5, 
					EFF_CROSS = 6, EFF_RAIN = 7, EFF_STAR = 8, EFF_SNAKE = 9, EFF_REC = 10,

					EFF_SPECTRUM = 11, EFF_RAPID_FIRE = 12, EFF_INDICATOR = 13, //mouse Eff

					EFF_MULTI_1 = 0xE0, EFF_MULTI_2 = 0xE1, EFF_MULTI_3 = 0xE2, EFF_MULTI_4 = 0xE3, 	                      
					EFF_OFF = 0xFE} EFF_INDEX;
//Enumeration of device list

typedef enum { DEV_MKeys_L = 0, DEV_MKeys_S = 1, DEV_MKeys_L_White = 2, DEV_MKeys_M_White = 3, DEV_MMouse_L = 4
					, DEV_MMouse_S = 5, DEV_MKeys_M = 6, DEV_MKeys_S_White = 7,} DEVICE_INDEX;

//Enumeration of device layout
typedef enum {LAYOUT_UNINIT = 0, LAYOUT_US = 1, LAYOUT_EU = 2} LAYOUT_KEYBOARD;


//extern "C" {
	/*
	@ Function Name: GetCM_SDK_DllVer
	@ Detail: Get SDK Dll's Version
	@ Variable:
	@ Returns: int : DLL's Version
	@ Note:
	@*/

	int GetCM_SDK_DllVer();


	///*****  System data related function      *****//

	/*
	@ Function Name: GetNowTime
	@ Detail: Obtain current system time
	@ Variable:
	@ Returns: TCHAR *: string index format is %Y %m/%d %H:%M %S
	@ Note:
	@*/
	TCHAR * GetNowTime();

	/*
	@ Function Name: GetNowCPUUsage
	@ Detail: obtain current CPU usuage ratio
	@ Variable:
	@ Returns: LONG: 0 ~ 100 integer
	@ Note:
	@*/
	LONG	GetNowCPUUsage();


	/*
	@ Function Name: GetRamUsage
	@ Detail: Obtain current RAM usuage ratio
	@ Variable:
	@ Returns: DWORD: 0 ~ 100 integer
	@ Note:
	@*/
	DWORD	GetRamUsage();



	/*
	@ Function Name: GetNowVolumePeekValue
	@ Detail: Obtain current volume
	@ Variable:
	@ Returns: float: 0 ~ 1 float number
	@ Note:
	@*/
	float	GetNowVolumePeekValue();


	///*****  Device operation function      *****//
	/*
	@ Function Name: SetControlDevic
	@ Detail: set operating device
	@ Variable: DEVICE_INDEX: device list DEV_MKeys_L, DEV_MKeys_S, DEV_MOUSE one among three(currently no mouse)
	@ Returns:
	@ Note:
	@*/
	void SetControlDevice(DEVICE_INDEX devIndex);


	/*
	@ Function Name: IsDevicePlug
	@ Detail: verify if the deviced is plugged in
	@ Variable:
	@ Returns: bool: true plugged in，false not plugged in
	@ Note:
	@*/
	bool	IsDevicePlug();


	/*
	@ Function Name: GetDeviceLayout
	@ Detail: Obtain current device layout
	@ Variable:
	@ Returns: Returns LAYOUT_KEYBOARD enum: currently 3 LAYOUT_UNINIT , LAYOUT_US , LAYOUT_EU
	@ Note:
	@*/
	LAYOUT_KEYBOARD GetDeviceLayout();

	/*
	@ Function Name: EnableLedControl
	@ Detail: set control over device’s LED
	@ Variable: bool bEnable: true Controlled by SW，false Controlled by FW
	@ Returns: bool: true Success，false Fail
	@ Note:
	@*/
	bool EnableLedControl(bool bEnable);



	/*
	@ Function Name: SwitchLedEffect
	@ Detail: switch device current effect
	@ Variable: EFF_INDEX iEffectIndex: index value of the effect
	@ Returns: bool: true Success，false Fail
	@ Note:
	@*/
	bool SwitchLedEffect(EFF_INDEX iEffectIndex);


	/*
	@ Function Name: RefreshLed
	@ Detail: Print out the lights setting from Buffer to LED
	@ Variable: bool bAuto: false means manual, call this function once, then print out once; true means auto, any light update will print out directly
	@ Returns: bool: true success ， false fail
	@ Note:
	@*/
	bool RefreshLed(bool bAuto/* = false*/);
	
	
	/*
	@ Function Name: SetFullLedColor
	@ Detail: set entire keyboard LED color
	@ Variable: BYTE r:red, BYTE g:green, BYTE b:blue
	@ Returns: bool: true Success，false Fail
	@ Note:
	@*/
	bool SetFullLedColor(BYTE r, BYTE g, BYTE b);


	/*
	@ Function Name: SetAllLedColor
	@ Detail: Set Keyboard "every LED" color
	@ Variable: COLOR_MATRIX colorMatrix:structure，fill up RGB value according to LED Table
	@ Returns: bool: true Success，false Fail
	@ Note:
	@*/
	bool SetAllLedColor(COLOR_MATRIX colorMatrix);


	/*
	@ Function Name: SetLedColor
	@ Detail: Set single Key LED color
	@ Variable: int iRow: row, int iColumn:column BYTE r:red, BYTE g:green, BYTE b:blue
	@ Returns: bool: true Success，false Fail
	@ Note:
	@*/
	bool SetLedColor(int iRow, int iColumn, BYTE r, BYTE g, BYTE b);


	/*
	@ Function Name: EnableKeyInterrupt
	@ Detail: To enable the call back function
	@ Variable: bool bEnable: true enable ，false disable
	@ Returns: bool: true sucess ， false fail
	@ Note: will call the call back function of SetKeyCallBack()
	@*/
	bool EnableKeyInterrupt(bool bEnable);


	/*
	@ Function Name: SetKeyCallBack
	@ Detail: Setup the call back function of button
	@ Variable: KEY_CALLBACK callback call back setup，please reference the def of KEY_CALLBACK
	@ Returns: none
	@ Note:
	@*/

	void SetKeyCallBack(KEY_CALLBACK callback);

//}