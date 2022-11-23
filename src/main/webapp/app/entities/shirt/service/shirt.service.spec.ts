import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IShirt, Shirt } from '../shirt.model';

import { ShirtService } from './shirt.service';

describe('Shirt Service', () => {
  let service: ShirtService;
  let httpMock: HttpTestingController;
  let elemDefault: IShirt;
  let expectedResult: IShirt | IShirt[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ShirtService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      colour1: 'AAAAAAA',
      colour2: 'AAAAAAA',
      type: 0,
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

    it('should create a Shirt', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Shirt()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Shirt', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          colour1: 'BBBBBB',
          colour2: 'BBBBBB',
          type: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Shirt', () => {
      const patchObject = Object.assign(
        {
          colour1: 'BBBBBB',
          type: 1,
        },
        new Shirt()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Shirt', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          colour1: 'BBBBBB',
          colour2: 'BBBBBB',
          type: 1,
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

    it('should delete a Shirt', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addShirtToCollectionIfMissing', () => {
      it('should add a Shirt to an empty array', () => {
        const shirt: IShirt = { id: 123 };
        expectedResult = service.addShirtToCollectionIfMissing([], shirt);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shirt);
      });

      it('should not add a Shirt to an array that contains it', () => {
        const shirt: IShirt = { id: 123 };
        const shirtCollection: IShirt[] = [
          {
            ...shirt,
          },
          { id: 456 },
        ];
        expectedResult = service.addShirtToCollectionIfMissing(shirtCollection, shirt);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Shirt to an array that doesn't contain it", () => {
        const shirt: IShirt = { id: 123 };
        const shirtCollection: IShirt[] = [{ id: 456 }];
        expectedResult = service.addShirtToCollectionIfMissing(shirtCollection, shirt);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shirt);
      });

      it('should add only unique Shirt to an array', () => {
        const shirtArray: IShirt[] = [{ id: 123 }, { id: 456 }, { id: 72504 }];
        const shirtCollection: IShirt[] = [{ id: 123 }];
        expectedResult = service.addShirtToCollectionIfMissing(shirtCollection, ...shirtArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shirt: IShirt = { id: 123 };
        const shirt2: IShirt = { id: 456 };
        expectedResult = service.addShirtToCollectionIfMissing([], shirt, shirt2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shirt);
        expect(expectedResult).toContain(shirt2);
      });

      it('should accept null and undefined values', () => {
        const shirt: IShirt = { id: 123 };
        expectedResult = service.addShirtToCollectionIfMissing([], null, shirt, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shirt);
      });

      it('should return initial array if no Shirt is added', () => {
        const shirtCollection: IShirt[] = [{ id: 123 }];
        expectedResult = service.addShirtToCollectionIfMissing(shirtCollection, undefined, null);
        expect(expectedResult).toEqual(shirtCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
