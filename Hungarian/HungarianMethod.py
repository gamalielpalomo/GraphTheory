from tkinter import Tk, RIGHT, BOTH, RAISED, Canvas, Frame, filedialog, W 
from tkinter.ttk import Frame, Button, Style
import copy

class nodo: #crea un objeto nodo que guardara el valor de cada elemento y si esta marcado
    def __init__(self, num):
        self.val=num
        self.marca=0

class matrizlineas: #crea un objeto matrizlineas que guardara el valor de la matriz y su numero de lineas
    def __init__(self, matriz,numerodelineas):
        self.val=matriz
        self.lineas=numerodelineas

class Graficador(Frame):
    
    def __init__(self):
        super().__init__()   
        self.initUI()

    def onOpen(self):
        ftypes = [('Python files', '*.txt'), ('All files', '*')]
        dlg = filedialog.Open(self, filetypes = ftypes)
        fl = dlg.show()

        if fl != '':
            text = self.readFile(fl)  
            self.l=[list(map(int,item.split())) for item in text.split('\n')]
            
            
    def readFile(self, filename):
        with open(filename, "r") as f:
            text = f.read()
        return text

    def initUI(self):
        self.master.title("Hungarian Algoritms")
        self.style = Style()
        self.style.theme_use("default")
        frame = Frame(self, relief=RAISED, borderwidth=1)
        frame.pack(fill=BOTH, expand=True)      
        self.pack(fill=BOTH, expand=True)

        canvas = Canvas(frame)
        
        closeButton = Button(self, text="Close")
        closeButton["command"] =  self.quit
        closeButton.pack(side=RIGHT, padx=5, pady=5)

        RunButton = Button(self, text="Draw",command= lambda: self.draw(canvas,self.l))
        RunButton.pack(side=RIGHT, padx=5, pady=5)

        UploadButton = Button(self, text="Upload")
        UploadButton["command"] = self.onOpen
        UploadButton.pack(side=RIGHT)              
    
    def draw(self,canvas,l):
            canvas.delete("all")
            print ("Corriendo!:",l)
            print(len(l),":",len(l[0]))
            python_green = "#476042"
            canvas.pack(fill=BOTH,expand=1)
            self.update()    
            print(canvas.winfo_width(),"-",canvas.winfo_height())
            
            width=canvas.winfo_width()
            height=canvas.winfo_height()

            pointsWidth=(width-200)/(len(l)+1)
            print(pointsWidth)
            pointsHeight=height/3
            print(pointsHeight)

            matriPosPaq = []
            matriPosSuc = []
            aux=copy.deepcopy(l)
            
            posiblesposiciones2=procesa(aux)

            for i in range(len(l)):
                posPaq=[pointsWidth*(i+1)-25,pointsHeight*(1)-25,pointsWidth*(i+1)+25,pointsHeight*(1)+25]
                posSuc=[pointsWidth*(i+1)-25,pointsHeight*(2)-25,pointsWidth*(i+1)+25,pointsHeight*(2)+25]
                matriPosPaq.insert(i,posPaq)
                matriPosSuc.insert(i,posSuc)

            for i in range(len(l)):
                for j in range(len(l[i])):
                    #if(l[i][j]!=0):
                    if [j,i] in posiblesposiciones2:
                        canvas.create_line(matriPosPaq[i][0]+30,matriPosPaq[i][1]+30, matriPosSuc[j][0]+30, matriPosSuc[j][1]+30,fill=python_green,width=3)
                    else: 
                        canvas.create_line(matriPosPaq[i][0]+30,matriPosPaq[i][1]+30, matriPosSuc[j][0]+30, matriPosSuc[j][1]+30)
                        #print(matriPosPaq[i][0]," ",matriPosSuc[i])

            for i in range(len(l)):
                canvas.create_oval(matriPosPaq[i],outline="#fb0", fill="#fb0", width=2)
                canvas.create_oval(matriPosSuc[i],outline="#fb0", fill="#fb0", width=2)
                canvas.create_text(matriPosPaq[i][0]+15,matriPosPaq[i][1]+25, anchor=W, font="Purisa",text=("P",i))
                canvas.create_text(matriPosSuc[i][0]+15, matriPosSuc[i][1]+25, anchor=W, font="Purisa",text=("S",i))

            canvas.create_text((width-200)/2-35, height/3-45, anchor=W, font="Purisa",text="Paquetes")
            canvas.create_text((width-200)/2-40, (height/3)*2+45, anchor=W, font="Purisa",text="Sucursales")

            heightMatriz=70
            weighMatriz =70

            canvas.create_text(((pointsWidth*len(l)+1)+(weighMatriz*(2))-5)+30, (heightMatriz*(2)-10)-90, anchor=W, font="Purisa",text="Paquetes")
            canvas.create_text((pointsWidth*len(l)+1)+(weighMatriz*(1)) -20, heightMatriz*(2+1), anchor=W, font="Purisa",text="Sucursales",angle=90)

            for i in range(len(l)):
                for j in range(len(l)):
                    if [j,i] in posiblesposiciones2:
                        canvas.create_oval((pointsWidth*len(l)+1)+(weighMatriz*(i+1))-5, heightMatriz*(j+1)-10,(pointsWidth*len(l)+1)+(weighMatriz*(i+1))+15, heightMatriz*(j+1)+10,outline="#fb0", fill="#fb0", width=2)
                        canvas.create_text((pointsWidth*len(l)+1)+(weighMatriz*(i+1)), heightMatriz*(j+1), anchor=W, font=("Purisa",15),text=l[j][i],fill=python_green)
                    else:   
                        canvas.create_text((pointsWidth*len(l)+1)+(weighMatriz*(i+1)), heightMatriz*(j+1), anchor=W, font="Purisa",text=l[j][i])
            
            canvas.pack(fill=BOTH,expand=1)

def rebalancea(MatrizMarcableRecibidaConLineas,valor):
    valor=valor
    MatrizMarcableRecibida=MatrizMarcableRecibidaConLineas.val
    NumerodeLineas=MatrizMarcableRecibidaConLineas.lineas
    valmin=0
    for x in range(0,valor):
        for j in range(0,valor):
            if MatrizMarcableRecibida[x][j].marca==0:
                if valmin==0:
                    valmin=MatrizMarcableRecibida[x][j].val
                valmin=min(valmin,MatrizMarcableRecibida[x][j].val)
    for x in range(0,valor):
        for j in range(0,valor):
            if MatrizMarcableRecibida[x][j].marca==0:
                MatrizMarcableRecibida[x][j].val-=valmin
    for x in range(0,valor):
            for j in range(0,valor):
                if MatrizMarcableRecibida[x][j].marca==2:
                    MatrizMarcableRecibida[x][j].val+=valmin
    MatrizMarcableRecibida=matrizlineas(MatrizMarcableRecibida,NumerodeLineas)
    return marca(MatrizMarcableRecibida,valor)

def marca(MatrizMarcableRecibidaConLineas,valor):
    valor=valor
    filas=[]
    columnas=[]
    MatrizMarcableRecibida=MatrizMarcableRecibidaConLineas.val
    NumerodeLineas=MatrizMarcableRecibidaConLineas.lineas
    for x in range(0,valor):#contrar ceros en las fila
        cont=0
        for j in range(0,valor):
            if MatrizMarcableRecibida[x][j].val==0 and MatrizMarcableRecibida[x][j].marca==0:
                cont+=1
        filas.append(cont)
    for x in range(0,valor):#contrar ceros en las columna
        cont=0
        for j in range(0,valor):
            if MatrizMarcableRecibida[j][x].val==0 and MatrizMarcableRecibida[j][x].marca==0:
                cont+=1
        columnas.append(cont)

    maxfila=max(filas)#obtiene el conteo maximo de ceros de las filas
    maxcolumna=max(columnas)#obtiene el conteo maximo de ceros de las columnas
    maxtotal=max(maxcolumna,maxfila)#obtiene el conteo maximo de ambos conteos
    #print("el numero maximo es"+str(maxtotal))
    for x in range(0,valor):#lineas para filas
        if filas[x]==maxtotal:
            for j in range(0,valor):
                MatrizMarcableRecibida[x][j].marca+=1
            NumerodeLineas+=1
    for x in range(0,valor):#lineas para columnas
        if columnas[x]==maxtotal and maxtotal!=1:
            for j in range(0,valor):
                MatrizMarcableRecibida[j][x].marca+=1
            NumerodeLineas+=1
    elementosmarcados=0
    for x in range(0,valor):#busca elementos no marcados
        for j in range(0,valor):
            if MatrizMarcableRecibida[x][j].marca==0 and MatrizMarcableRecibida[x][j].val==0:
                elementosmarcados=1
                break
    if elementosmarcados==0:
        MatrizMarcableRecibida=matrizlineas(MatrizMarcableRecibida,NumerodeLineas)
        return MatrizMarcableRecibida
    else:
        MatrizMarcableRecibida=matrizlineas(MatrizMarcableRecibida,NumerodeLineas)
        return marca(MatrizMarcableRecibida,valor)

def procesa(entrada):
    Mentrada = copy.copy(entrada)

    valor=len(Mentrada)
    
    MatrizMarcable=[]

    for x in range(0,valor):#resta el elemento menor a cada fila
        min2=min(Mentrada[x])
        for j in range(0,valor):
            Mentrada[x][j]-=min2

    for x in range(0,valor):#resta el elemento menor a cada columna
        aux = []
        for j in range(0,valor):
            aux.append(Mentrada[j][x])
        min2=min(aux)
        for j in range(0,valor):
            Mentrada[j][x]-=min2

    for x in range(0,valor):#recrea la matriz con nodos donde se guardan los valores y pueden ser marcados
        pre=[]
        for j in range(0,valor):
            uno = nodo(Mentrada[x][j])
            pre.append(uno)
        MatrizMarcable.append(pre)

    MatrizMarcable=matrizlineas(MatrizMarcable,0)
    while MatrizMarcable.lineas < valor :
        MatrizMarcable=marca(MatrizMarcable,valor)
        if MatrizMarcable.lineas < valor:
            MatrizMarcable=rebalancea(MatrizMarcable,valor)


    MatrizMarcable=MatrizMarcable.val

    numerodemarcasenlamatriz=0
    while(numerodemarcasenlamatriz!=valor):
        for x in range(0,valor):
            for j in range(0,valor):
                if MatrizMarcable[x][j].marca==1 and MatrizMarcable[x][j].val==0:
                    cont=0
                    for q in range(x,valor):
                        if MatrizMarcable[q][j].marca==1 and MatrizMarcable[x][j].val==0:
                            cont+=1
                    if cont==1:
                        for q in range(0,valor):
                            if x!=q:
                                MatrizMarcable[q][j].marca+=10
                    else:
                        break
        cuentamarcas=0
        for x in range(0,valor):
            for j in range(0,valor):
                if MatrizMarcable[x][j].marca==1 and MatrizMarcable[x][j].val==0 :
                    cuentamarcas+=1
        numerodemarcasenlamatriz=cuentamarcas
    posiblesposiciones=[]

    for x in range(0,valor):
        for j in range(0,valor):
            if MatrizMarcable[x][j].marca==1 and MatrizMarcable[x][j].val==0:
                posiblesposiciones.append([x,j])
    #print(posiblesposiciones)

    return (posiblesposiciones)

def main():
    #print(procesa([[13,7,2,1],[5,18,8,6],[8,3,11,15],[4,2,5,3]]))
    root = Tk()
    root.geometry("850x400+300+50")
    app = Graficador()
    root.mainloop()  

if __name__ == '__main__':
    main()  