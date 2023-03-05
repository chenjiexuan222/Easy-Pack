#include<reg52.h>
#include<stdio.h>

void operate(int speed, int pulse);
void delay(int x);
unsigned char sum_check(unsigned char dataBuf[],unsigned char len);
void operateX(int speed, int pulse);
void operateY(int speed, int pulse);
void operateZ(int speed, int pulse);
void operateH(int speed, int pulse);
void operateP(int speed, int pulse);
void send_char(unsigned char buf);
void Delay500ms();
void Delay3000ms();

sbit P1_0 = P1^0;
sbit P1_1 = P1^1;
sbit P1_2 = P1^2;
sbit P2_2 = P2^2;
sbit P2_5 = P2^5;
sbit P2_6 = P2^6;
sbit P2_7 = P2^7;

sbit P0_0 = P0^0;
sbit P0_1 = P0^1;
sbit P0_2 = P0^2;
sbit P0_3 = P0^3;
sbit P0_4 = P0^4;
sbit P0_5 = P0^5;

sbit P3_2 = P3^2;
sbit P3_5 = P3^5;

unsigned char UART_BUF[64];
unsigned int buf_count = 0;
unsigned int Speed = 0;
unsigned int Pulse = 0;
//unsigned char Dir = 0;
unsigned int function_state	= 0;//功能码用于对应不同的功能：1/2/3/4 ----> XYZ三轴移动及与HPLC建立通信
unsigned int serial_info_state	= 0;


/*???? ????P4 LED???*/ 
sfr P4 = 0xC0;
sfr P4SW = 0xBB;
sbit P4_5 = P4^5;	//?????LED

void sys_Init()
{
	int i;
	TMOD = 0x20;    //?????1???÷??2
	TH1 = 0xFD;     //??????λ
	TL1 = 0xFD;		//??????λ
	SCON = 0x50;    //?趨???п???????????1 
	TR1 = 1;        //?????????1
	EX0 = 1;	    //???????ж?
	ES  = 1;        //?????????ж?.  
	EA  = 1;	    //????????ж????
	//TI = 1;

	P4SW |= 0x70;

	for(i = 0; i < 4; i++)
	{
		P4_5 = ~P4_5;
		Delay500ms();
	}

}

void main()
{
	sys_Init();

	while(1)
	{
	    if(serial_info_state == 1)
		{
			P0_0 = 1;
			P0_3 = 1;
			P2_5 = 0;
		  operate(Speed,Pulse);
			//printf("%d",Speed);
			//printf("%d",Pulse);
		}
	}	
}

void ser_int() interrupt 4
{
	
	if(RI == 1)
	{
		P4_5 = ~P4_5;
		UART_BUF[buf_count] = SBUF;
		if(buf_count == 0 && UART_BUF[buf_count] == 0xaa)//header
		{
			buf_count = 1;	
		}
		else if(buf_count == 1)	 //mutiple function
		{		
			if(UART_BUF[buf_count] == 0x01){//X轴移动
          function_state = 1;
      }else if(UART_BUF[buf_count] == 0x02){//Y轴移动
          function_state = 2;
      }else if(UART_BUF[buf_count] == 0x03){//Z轴移动
          function_state = 3;
      }else if(UART_BUF[buf_count] == 0x04){//Hplc的io信号触发
          function_state = 4;
      }else if(UART_BUF[buf_count] == 0x05){//perial pump
          function_state = 5;
      }
      buf_count = 2;
		}							 
		else if(buf_count == 2)	 //speed(high)
		{
			buf_count = 3;
		}
		else if(buf_count == 3)	 //speed(low)
		{
			buf_count = 4;
		}
		else if(buf_count == 4)	 //pulse(high)
		{
			buf_count = 5;
		}
		else if(buf_count == 5)	 //pulse(low)
		{
			buf_count = 6;
		}
		else if(buf_count == 6 && (UART_BUF[buf_count] == 0x00 || UART_BUF[buf_count] == 0x01))	 //dir
		{
			if (UART_BUF[buf_count] == 0x00){
				P0_2 = 0;
				P0_5 = 0;
				P2_7 = 0;
			}else{
				P0_2 = 1;
				P0_5 = 1;
				P2_7 = 1;
			}
			buf_count = 7;
		}
		else if(buf_count == 7 && UART_BUF[buf_count] == 0x55)//end
		{
			buf_count = 8;
		}
		else if(buf_count == 8) //sum-check	
		{
			if(sum_check(UART_BUF,8) == UART_BUF[8]){
         Speed = UART_BUF[2]*256 + UART_BUF[3];
         Pulse = UART_BUF[4]*256 + UART_BUF[5];
         //Dir = UART_BUF[6];
         serial_info_state = 1;
      }
      buf_count = 0;
		}
		else
		{
			buf_count = 0;	
		}
		RI = 0;
	}
}

void operate(int speed, int pulse)
{
	 serial_info_state = 0;
	 send_char('S');

     if(function_state == 1){
         operateX(speed,pulse);
     }else if(function_state == 2){
         operateY(speed,pulse);
     }else if(function_state == 3){
         operateZ(speed,pulse);
     }else if(function_state == 4){
         operateH(speed,pulse);
     }else if(function_state == 5){
         operateP(speed,pulse);
     }

     function_state = 0;
	
	 send_char('E');
}

void operateX(int speed, int pulse){
    int i;

    if(pulse > 0){
			for(i = 0; i < pulse; i++){
				if(P1_0 == 1){
					P0_1 = 0;
					delay(speed);
					//Delay500ms();
					P0_1 = 1;
					delay(speed);
					//Delay500ms();
				}else if(P1_0 == 0){
						if(P0_2 == 1){
							P0_1 = 0;
							delay(speed);
							//Delay500ms();
							P0_1 = 1;
							delay(speed);
							//Delay500ms();
						}else{
							break;
						}
					}  
			}
	 }
}

void operateY(int speed, int pulse){
    int i;

		if(pulse > 0){
			for(i = 0; i < pulse; i++){
				if(P1_1 == 1){
					P0_4 = 0;
					delay(speed);
					P0_4 = 1;
					delay(speed);
				}else if(P1_1 == 0){
						if(P0_5 == 1){
							P0_4 = 0;
							delay(speed);
							P0_4 = 1;
							delay(speed);
						}else{
							break;
						}
					}  
			}
	 }
}

void operateZ(int speed, int pulse){
    int i;

  if(pulse > 0){
			for(i = 0; i < pulse; i++){
				if(P1_2 == 1){
					P2_6 = 0;
					delay(speed);
					P2_6 = 1;
					delay(speed);
				}else if(P1_2 == 0){
						if(P2_7 == 1){
							P2_6 = 0;
							delay(speed);
							P2_6 = 1;
							delay(speed);
						}else{
							break;
						}
					}  
			}
	 }  
}

void operateH(int speed, int pulse){
	if(speed == 1 && pulse == 1){
		P2_2 = 0;
		Delay3000ms();
		P2_2 = 1;
	}
}
	
void operateP(int speed, int pulse){
	if(speed == 1 && pulse == 1){
			P3_5 = 0;
		  Delay3000ms();
		  P3_5 = 1;
  }
}

void send_char(unsigned char buf)  //发送一个字符
{
		SBUF = buf;
	  while(!TI);	 //即while（）用查询的方式等待发送完成，发送完成TI会置1，因此跳出循环即表示发送完成，随后将TI置0
		TI = 0;
}

//累加和校验
unsigned char sum_check(unsigned char dataBuf[],unsigned char len)
{
	
    unsigned char sum = 0;
    for(;len > 0; len--)
    {
        sum += dataBuf[len-1];
    }
    return sum;
}




void delay(int x)
{	
	int j;
	for(j = 0; j < x; j++);
}

void Delay500ms()		//@11.0592MHz
{
	unsigned char i, j, k;
	i = 22;
	j = 3;
	k = 227;
	do
	{
		do
		{
			while (--k);
		} while (--j);
	} while (--i);
}
	void Delay3000ms()		//@11.0592MHz
{
	unsigned char i, j, k;
	i = 127;
	j = 18;
	k = 107;
	do
	{
		do
		{
			while (--k);
		} while (--j);
	} while (--i);
}