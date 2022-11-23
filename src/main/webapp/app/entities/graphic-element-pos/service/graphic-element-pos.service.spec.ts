import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGraphicElementPos, GraphicElementPos } from '../graphic-element-pos.model';

import { GraphicElementPosService } from './graphic-element-pos.service';

describe('GraphicElementPos Service', () => {
  let service: GraphicElementPosService;
  let httpMock: HttpTestingController;
  let elemDefault: IGraphicElementPos;
  let expectedResult: IGraphicElementPos | IGraphicElementPos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GraphicElementPosService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      x: 0,
      y: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a GraphicElementPos', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new GraphicElementPos()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GraphicElementPos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          x: 1,
          y: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GraphicElementPos', () => {
      const patchObject = Object.assign({}, new GraphicElementPos());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GraphicElementPos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          x: 1,
          y: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a GraphicElementPos', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGraphicElementPosToCollectionIfMissing', () => {
      it('should add a GraphicElementPos to an empty array', () => {
        const graphicElementPos: IGraphicElementPos = { id: 123 };
        expectedResult = service.addGraphicElementPosToCollectionIfMissing([], graphicElementPos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(graphicElementPos);
      });

      it('should not add a GraphicElementPos to an array that contains it', () => {
        const graphicElementPos: IGraphicElementPos = { id: 123 };
        const graphicElementPosCollection: IGraphicElementPos[] = [
          {
            ...graphicElementPos,
          },
          { id: 456 },
        ];
        expectedResult = service.addGraphicElementPosToCollectionIfMissing(graphicElementPosCollection, graphicElementPos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GraphicElementPos to an array that doesn't contain it", () => {
        const graphicElementPos: IGraphicElementPos = { id: 123 };
        const graphicElementPosCollection: IGraphicElementPos[] = [{ id: 456 }];
        expectedResult = service.addGraphicElementPosToCollectionIfMissing(graphicElementPosCollection, graphicElementPos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(graphicElementPos);
      });

      it('should add only unique GraphicElementPos to an array', () => {
        const graphicElementPosArray: IGraphicElementPos[] = [{ id: 123 }, { id: 456 }, { id: 74366 }];
        const graphicElementPosCollection: IGraphicElementPos[] = [{ id: 123 }];
        expectedResult = service.addGraphicElementPosToCollectionIfMissing(graphicElementPosCollection, ...graphicElementPosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const graphicElementPos: IGraphicElementPos = { id: 123 };
        const graphicElementPos2: IGraphicElementPos = { id: 456 };
        expectedResult = service.addGraphicElementPosToCollectionIfMissing([], graphicElementPos, graphicElementPos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(graphicElementPos);
        expect(expectedResult).toContain(graphicElementPos2);
      });

      it('should accept null and undefined values', () => {
        const graphicElementPos: IGraphicElementPos = { id: 123 };
        expectedResult = service.addGraphicElementPosToCollectionIfMissing([], null, graphicElementPos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(graphicElementPos);
      });

      it('should return initial array if no GraphicElementPos is added', () => {
        const graphicElementPosCollection: IGraphicElementPos[] = [{ id: 123 }];
        expectedResult = service.addGraphicElementPosToCollectionIfMissing(graphicElementPosCollection, undefined, null);
        expect(expectedResult).toEqual(graphicElementPosCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
