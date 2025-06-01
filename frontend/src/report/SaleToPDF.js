import pdfMake from 'pdfmake/build/pdfmake'
import pdfFonts from 'pdfmake/build/vfs_fonts'
import { convertNumberToCurrency } from "../utils/convertNumberToCurrency";

function SaleToPDF(saleList) {

    pdfMake.vfs = pdfFonts.pdfMake.vfs;

    const reportTitle = [
        {
            text: 'Relatório de Vendas',
            fontSize: 15,
            bold: true,
            alignment: 'center',
            margin: [0, 20, 0, 20]
        }
    ];

    const dados = saleList.map((sale) => {
        return [
            {
                text: sale.id,
                fontSize: 9,
                margin: [0, 2, 0, 2]
            },
            {
                text: sale.clienteNome,
                fontSize: 9,
                margin: [0, 2, 0, 2]
            },
            {
                text: sale.livro.titulo,
                fontSize: 9,
                margin: [0, 2, 0, 2]
            },
            {
                text: sale.qtdItens,
                fontSize: 9,
                margin: [0, 2, 0, 2]
            },
            {
                text: convertNumberToCurrency(sale.precoVenda),
                fontSize: 9,
                margin: [0, 2, 0, 2]
            },
            {
                text: convertNumberToCurrency(sale.total),
                fontSize: 9,
                margin: [0, 2, 0, 2]
            },
        ]
    });

    const details = [
        {
            table: {
                headerRows: 1,
                widths: ['*', '*', '*', '*', '*', '*'],
                body: [
                    [
                        {
                            text: 'Id',
                            style: 'tableHeader',
                            fontSize: 10,
                            bold: true
                        },
                        {
                            text: 'Cliente',
                            style: 'tableHeader',
                            fontSize: 10,
                            bold: true
                        },
                        {
                            text: 'Livro',
                            style: 'tableHeader',
                            fontSize: 10,
                            bold: true
                        },
                        {
                            text: 'Quantidade',
                            style: 'tableHeader',
                            fontSize: 10,
                            bold: true
                        },
                        {
                            text: 'Preço de Venda',
                            style: 'tableHeader',
                            fontSize: 10,
                            bold: true
                        },
                        {
                            text: 'Total',
                            style: 'tableHeader',
                            fontSize: 10,
                            bold: true
                        },
                    ],
                    ...dados
                ]
            },
            margin: [0, 20, 0, 20],
            alignment: 'center',
            layout: 'lightHorizontalLines'
        }
    ];

    function Rodape(currentPage, pageCount) {
        return [
            {
                text: currentPage + ' / ' + pageCount,
                alignment: 'right',
                fontSize: 9,
                margin: [0, 10, 20, 0]
            }
        ]
    }

    const docDefinitios = {
        pageSize: 'A4',
        paggeMargins: [15, 50, 15, 40],
        header: [reportTitle],
        content: [details],
        footer: Rodape
    }

    pdfMake.createPdf(docDefinitios).download();

}

export default SaleToPDF;