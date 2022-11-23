import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPosition, Position } from '../position.model';

import { PositionService } from './position.service';

describe('Position Service', () => {
  let service: PositionService;
  let httpMock: HttpTestingController;
  let elemDefault: IPosition;
  let expectedResult: IPosition | IPosition[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PositionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      abb: 'AAAAAAA',
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

    it('should create a Position', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Position()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Position', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          abb: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Position', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          abb: 'BBBBBB',
        },
        new Position()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Position', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          abb: 'BBBBBB',
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

    it('should delete a Position', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPositionToCollectionIfMissing', () => {
      it('should add a Position to an empty array', () => {
        const position: IPosition = { id: 123 };
        expectedResult = service.addPositionToCollectionIfMissing([], position);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(position);
      });

      it('should not add a Position to an array that contains it', () => {
        const position: IPosition = { id: 123 };
        const positionCollection: IPosition[] = [
          {
            ...position,
          },
          { id: 456 },
        ];
        expectedResult = service.addPositionToCollectionIfMissing(positionCollection, position);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Position to an array that doesn't contain it", () => {
        const position: IPosition = { id: 123 };
        const positionCollection: IPosition[] = [{ id: 456 }];
        expectedResult = service.addPositionToCollectionIfMissing(positionCollection, position);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(position);
      });

      it('should add only unique Position to an array', () => {
        const positionArray: IPosition[] = [{ id: 123 }, { id: 456 }, { id: 43914 }];
        const positionCollection: IPosition[] = [{ id: 123 }];
        expectedResult = service.addPositionToCollectionIfMissing(positionCollection, ...positionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const position: IPosition = { id: 123 };
        const position2: IPosition = { id: 456 };
        expectedResult = service.addPositionToCollectionIfMissing([], position, position2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(position);
        expect(expectedResult).toContain(position2);
      });

      it('should accept null and undefined values', () => {
        const position: IPosition = { id: 123 };
        expectedResult = service.addPositionToCollectionIfMissing([], null, position, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(position);
      });

      it('should return initial array if no Position is added', () => {
        const positionCollection: IPosition[] = [{ id: 123 }];
        expectedResult = service.addPositionToCollectionIfMissing(positionCollection, undefined, null);
        expect(expectedResult).toEqual(positionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
