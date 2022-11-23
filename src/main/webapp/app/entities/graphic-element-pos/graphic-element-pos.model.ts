export interface IGraphicElementPos {
  id?: number;
  name?: string;
  x?: number | null;
  y?: number | null;
  parent?: IGraphicElementPos | null;
  children?: IGraphicElementPos[] | null;
}

export class GraphicElementPos implements IGraphicElementPos {
  constructor(
    public id?: number,
    public name?: string,
    public x?: number | null,
    public y?: number | null,
    public parent?: IGraphicElementPos | null,
    public children?: IGraphicElementPos[] | null
  ) {}
}

export function getGraphicElementPosIdentifier(graphicElementPos: IGraphicElementPos): number | undefined {
  return graphicElementPos.id;
}
