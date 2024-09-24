module adder(
    input [3:0] x, y,      
    input Load, Clock,     
    output [3:0] S         
);
    wire SI, SO1, SO2;     
    
    shiftreg r1(.SI(SI), .PI(x), .SO(SO1), .PO(S), .Clock(Clock), .Load(Load));
    shiftreg r2(.SI(1'b0), .PI(y), .SO(SO2), .PO(), .Clock(Clock), .Load(Load));
    serial_adder sa(.x(SO1), .y(SO2), .S(SI), .Clock(Clock), .Clear(Load));
endmodule

module D_flip_flop(D,CLK,Q);
   input D,CLK;
   output Q;
   wire CLK1, Y;
   not  not1 (CLK1,CLK);
   D_latch D1(D,CLK, Y),
           D2(Y,CLK1,Q);
endmodule

module D_latch(D,C,Q);
   input D,C;
   output Q;
   wire x,y,D1,Q1;
   nand nand1 (x,D, C),
        nand2 (y,D1,C),
        nand3 (Q,x,Q1),
        nand4 (Q1,y,Q);
   not  not1  (D1,D);
endmodule

module mux2to1(
    input sel, in0, in1, 
    output out           
);
    wire not_sel, and0, and1; 
    
    not u1(not_sel, sel);
    and u2(and0, in0, not_sel);
    and u3(and1, in1, sel);
    or u4(out, and0, and1);
endmodule

module shiftreg(
    input SI,             
    input [3:0] PI,       
    output SO,            
    output [3:0] PO,      
    input Clock, Load     
);
    wire [3:0] mux_out;   
    
    D_flip_flop dff0(.D(mux_out[0]), .CLK(Clock), .Q(PO[0]));
    D_flip_flop dff1(.D(mux_out[1]), .CLK(Clock), .Q(PO[1]));
    D_flip_flop dff2(.D(mux_out[2]), .CLK(Clock), .Q(PO[2]));
    D_flip_flop dff3(.D(mux_out[3]), .CLK(Clock), .Q(PO[3]));
    
    mux2to1 mux0(.sel(Load), .in0(PO[1]), .in1(PI[0]), .out(mux_out[0]));
    mux2to1 mux1(.sel(Load), .in0(PO[2]), .in1(PI[1]), .out(mux_out[1]));
    mux2to1 mux2(.sel(Load), .in0(PO[3]), .in1(PI[2]), .out(mux_out[2]));
    mux2to1 mux3(.sel(Load), .in0(SI), .in1(PI[3]), .out(mux_out[3]));
    
    assign SO = PO[0];    
endmodule

module serial_adder(
    input x, y,           
    input Clock, Clear,   
    output S              
);
    wire sum, carry, mux_out, D;  
    
    full_adder fa(.a(x), .b(y), .cin(D), .sum(sum), .cout(carry));
    
    D_flip_flop dff(.D(mux_out), .CLK(Clock), .Q(D));
    
    mux2to1 mux(.sel(Clear), .in0(carry), .in1(1'b0), .out(mux_out));
    
    assign S = sum;       
endmodule

module full_adder(
    input a, b, cin,      
    output sum, cout      
);
    wire s1, c1, c2;      
    
    xor (s1, a, b);
    xor (sum, s1, cin);
    and (c1, a, b);
    and (c2, s1, cin);
    or  (cout, c1, c2);
endmodule

module test;
    reg signed [3:0] A, B;  
    reg Load, Clock, Clear;  
    wire signed [3:0] S;     
    
    adder add(.x(A), .y(B), .S(S), .Load(Load), .Clock(Clock));
    
    always #1 Clock = ~Clock;
    
    initial begin
        A = 0; 
        B = 7;
        Load = 1;
        Clock = 1;
        Clear = 0;
        #2 Load = 0;
        #8 $display("%d + %d = %d", A, B, S);
        $finish;
    end
endmodule
