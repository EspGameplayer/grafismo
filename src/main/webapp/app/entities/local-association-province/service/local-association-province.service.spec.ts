import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILocalAssociationProvince, LocalAssociationProvince } from '../local-association-province.model';

import { LocalAssociationProvinceService } from './local-association-province.service';

describe('LocalAssociationProvince Service', () => {
  let service: LocalAssociationProvinceService;
  let httpMock: HttpTestingController;
  let elemDefault: ILocalAssociationProvince;
  let expectedResult: ILocalAssociationProvince | ILocalAssociationProvince[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LocalAssociationProvinceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a LocalAssociationProvince', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LocalAssociationProvince()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LocalAssociationProvince', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LocalAssociationProvince', () => {
      const patchObject = Object.assign({}, new LocalAssociationProvince());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LocalAssociationProvince', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a LocalAssociationProvince', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLocalAssociationProvinceToCollectionIfMissing', () => {
      it('should add a LocalAssociationProvince to an empty array', () => {
        const localAssociationProvince: ILocalAssociationProvince = { id: 123 };
        expectedResult = service.addLocalAssociationProvinceToCollectionIfMissing([], localAssociationProvince);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(localAssociationProvince);
      });

      it('should not add a LocalAssociationProvince to an array that contains it', () => {
        const localAssociationProvince: ILocalAssociationProvince = { id: 123 };
        const localAssociationProvinceCollection: ILocalAssociationProvince[] = [
          {
            ...localAssociationProvince,
          },
          { id: 456 },
        ];
        expectedResult = service.addLocalAssociationProvinceToCollectionIfMissing(
          localAssociationProvinceCollection,
          localAssociationProvince
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LocalAssociationProvince to an array that doesn't contain it", () => {
        const localAssociationProvince: ILocalAssociationProvince = { id: 123 };
        const localAssociationProvinceCollection: ILocalAssociationProvince[] = [{ id: 456 }];
        expectedResult = service.addLocalAssociationProvinceToCollectionIfMissing(
          localAssociationProvinceCollection,
          localAssociationProvince
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(localAssociationProvince);
      });

      it('should add only unique LocalAssociationProvince to an array', () => {
        const localAssociationProvinceArray: ILocalAssociationProvince[] = [{ id: 123 }, { id: 456 }, { id: 75006 }];
        const localAssociationProvinceCollection: ILocalAssociationProvince[] = [{ id: 123 }];
        expectedResult = service.addLocalAssociationProvinceToCollectionIfMissing(
          localAssociationProvinceCollection,
          ...localAssociationProvinceArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const localAssociationProvince: ILocalAssociationProvince = { id: 123 };
        const localAssociationProvince2: ILocalAssociationProvince = { id: 456 };
        expectedResult = service.addLocalAssociationProvinceToCollectionIfMissing([], localAssociationProvince, localAssociationProvince2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(localAssociationProvince);
        expect(expectedResult).toContain(localAssociationProvince2);
      });

      it('should accept null and undefined values', () => {
        const localAssociationProvince: ILocalAssociationProvince = { id: 123 };
        expectedResult = service.addLocalAssociationProvinceToCollectionIfMissing([], null, localAssociationProvince, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(localAssociationProvince);
      });

      it('should return initial array if no LocalAssociationProvince is added', () => {
        const localAssociationProvinceCollection: ILocalAssociationProvince[] = [{ id: 123 }];
        expectedResult = service.addLocalAssociationProvinceToCollectionIfMissing(localAssociationProvinceCollection, undefined, null);
        expect(expectedResult).toEqual(localAssociationProvinceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
